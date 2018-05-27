package youness.automotive.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import youness.automotive.controller.bean.*;
import youness.automotive.repository.model.BaseEntity;
import youness.automotive.utils.BeanContainerUtils;
import youness.automotive.utils.StringUtils;

import javax.validation.ValidationException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 * <p>
 * The generic interface used on controller that want to use the automated list and edit view creation
 *
 * @param <T> the entity type
 */
public interface GenericViewController<T extends BaseEntity> {
    String SELECT_POST_FORM_VALUE_PREFIX = "asyncPostFormSelectField_";
    String SELECT_POST_FORM_OWNER_TYPE_PREFIX = "asyncPostFormParentFieldType_";
    String SELECT_POST_FORM_CHILD_TYPE_PREFIX = "asyncPostFormChildFieldType_";
    String SELECT_POST_FORM_OWNER_PREFIX = "asyncPostFormSelectParentFieldValue_";

    Logger logger = LoggerFactory.getLogger(GenericViewController.class);

    /**
     * The name of the root folder view under templates folder
     *
     * @return
     */
    default String getRootViewName() {
        return "views";
    }

    /**
     * The repository to handle the controller's entity
     *
     * @return
     */
    JpaRepository<T, Long> getRepository();

    /**
     * The class that this controller belongs to
     *
     * @return
     */
    Class<T> getParentClass();

    /**
     * The title to be previewed on  create/edit/list views
     *
     * @return
     */
    String getViewTitle();

    /**
     * The ordered list of columns/properties and their caption providers that will be used the list (table) view
     * table's view
     * Also, these properties are used to build the Add or Edit (header part) view
     *
     * @return TODO make it through reflection/annotation as the default behaviour?
     */
    List<PropertyMetadata<T>> getPropertyMetadata();

    /**
     * The linked property containers to be previewed on the view
     * This represents the OneToMany relations in entity mapping
     *
     * @param beanId the optional bean/entity ID that the containers should be built for
     * @return
     */
    List<LinkedPropertyContainer> getLinkedPropertyContainers(Long beanId);

    /**
     * Should save the link between entities according to the bean value
     *
     * @param bean           Represents the request bean that is parsed from save link request to join a child link to its parent
     * @param linkUniqueName the unique name set on LinkedPropertyContainer's propertyName when containers were created and
     *                       returned through;
     *                       {@link youness.automotive.controller.GenericViewController#getLinkedPropertyContainers})
     * @return the caption of the new added entity to be added to the table or null in case of any error
     * @throws IllegalArgumentException  when link with unique name is not recognized
     * @throws InvalidParameterException when there is a business logic error
     */
    String handleSaveDataLinkRequest(DataLinkRequestBean bean, String linkUniqueName)
            throws IllegalArgumentException, InvalidParameterException;

    /**
     * The method to handle view responsible to list the entities in a table
     *
     * @param model
     * @return
     */
    @RequestMapping("/list")
    default String list(Model model) {
        List<T> entities = getRepository().findAll();
        List<BeanContainer> beanContainers = BeanContainerUtils.createBeanContainers(entities, getPropertyMetadata());

        // Used to populate row values in the view's table
        model.addAttribute("list", beanContainers);
        // Used to populate column names in the view's table
        model.addAttribute("entityPropertyNames", getColumnTitles());
        // TODO make above attributes into one

        model.addAttribute("menuActions", getOptionalActions());
        String title = getListTitle();
        populatePageMetadata(model, title);
        return getListViewPath();
    }

    /**
     * Redirection point to redirect to add new entity
     *
     * @param model
     * @return
     */
    @RequestMapping("/add")
    default String add(Model model) {
        T entity = BeanUtils.instantiateClass(getParentClass());
        model.addAttribute("bean", entity);

        populatePageMetadata(model, "Add " + getViewTitle());
        populatePropertyMetadata(model, entity);
        return getAlterViewPath();
    }

    @RequestMapping(value = "/edit")
    default String edit(@RequestParam("id") Long beanId, Model model) {
        Optional<T> optional = getRepository().findById(beanId);
        if (optional.isPresent()) {
            T entity = optional.get();
            model.addAttribute("bean", entity);

            populatePageMetadata(model, "Edit " + getViewTitle());
            populatePropertyMetadata(model, entity);
            populateLinkedProperties(model, beanId);
            return getAlterViewPath();
        } else {
            return getErrorViewPath();
        }
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    default String save(T bean) {
        try {
            validateBeforeSave(bean);
        } catch (ValidationException e) {
            return getErrorViewPath();// TODO add parameter to show the error reason to user
        }

        if (bean.getId() != null) { // existing entity is updated
            Optional<T> optionalEntity = getRepository().findById(bean.getId());
            if (optionalEntity.isPresent()) {
                T entity = optionalEntity.get();

                // getPropertyMetadata() is used in populatePropertyMetadata() to populate the edit/add view. Therefore,
                // the input parameter input bean should be only considered for those properties returned by
                // getPropertyMetadata()
                BeanContainerUtils.copyProperties(bean, entity, getPropertyNames());
                getRepository().save(entity);
            } else {
                return getErrorViewPath();
            }
        } else { // new entity is added
            getRepository().save(bean);
        }

        return getListPath();
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    default String delete(@PathVariable("id") Long beanId) {
        getRepository().deleteById(beanId);
        return getListPath();
    }

    @RequestMapping(value = "/saveLink", method = RequestMethod.POST)
    // TODO change to PUT. PUT is idempotent and linking too :)
    default ResponseEntity saveLink(@RequestBody String postPayload) {
        DataLinkRequestBean bean;
        try {
            String[] parameters = postPayload.split("&");
            String selectedValue = getParameterValue(parameters, SELECT_POST_FORM_VALUE_PREFIX);
            String ownerId = getParameterValue(parameters, SELECT_POST_FORM_OWNER_PREFIX);
            String ownerType = getParameterValue(parameters,
                    SELECT_POST_FORM_OWNER_TYPE_PREFIX);// TODO remove from controller and view
            String childType = getParameterValue(parameters,
                    SELECT_POST_FORM_CHILD_TYPE_PREFIX);// TODO remove from controller and view

            String linkUniqueName = ownerId.split("=")[0];
            String ownerStringId = ownerId.split("=")[1];
            assert linkUniqueName.equals(selectedValue.split("=")[0]);
            String childStringId = selectedValue.split("=")[1];
            bean = new DataLinkRequestBean(Long.parseLong(ownerStringId), Long.parseLong(childStringId));

            String responseString;
            try {
                responseString = handleSaveDataLinkRequest(bean, linkUniqueName);
            } catch (InvalidParameterException e) {
                DataLinkResponseBean result = new DataLinkResponseBean();
                result.setMessage(e.getMessage());
                result.setError(true);
                return ResponseEntity.ok(result);
            }

            DataLinkResponseBean result = new DataLinkResponseBean();
            if (responseString != null) {
                result.setMessage("Added successfully!");
                result.setEntity(responseString);
            } else {
                result.setMessage("Already added!"); // TODO create a new bean to handle different cases
                result.setError(true);
            }

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Invalid save-link request received: " + postPayload);// TODO check the security threats
            return createResponseEntityForBadLinkRequest("Invalid request!");// TODO log the request
        }

    }

    default ResponseEntity createResponseEntityForBadLinkRequest(String message) {
        DataLinkResponseBean result = new DataLinkResponseBean();
        result.setMessage(message);
        result.setError(true);
        return ResponseEntity.badRequest().body(result);
    }

    default String getParameterValue(String[] parameters, String parameterName) throws InvalidParameterException {
        Optional<String> optionalSelectedValue =
                Stream.of(parameters).filter(parameter -> parameter.startsWith(parameterName)).findFirst();
        if (!optionalSelectedValue.isPresent()) {
            throw new InvalidParameterException();
        }
        // Remove the prefix identifier and return the value
        return optionalSelectedValue.get().replaceFirst(parameterName, "");
    }

    default List<String> getPropertyNames() {
        return getPropertyMetadata().stream().filter(
                propertyMetadata -> !propertyMetadata.isTransient())
                .map(PropertyMetadata::getName).collect(Collectors.toList());
    }

    default Collection<String> getColumnTitles() {
        return getPropertyMetadata().stream().map(PropertyMetadata::getTitle).collect(Collectors.toList());
    }

    default void populatePageMetadata(Model model, String title) {
        model.addAttribute("pageMetadata", new PageMetaData(title, getRootViewName()));
    }

    default void populatePropertyMetadata(Model model, T entity) {
        model.addAttribute("propertyContainers", getPropertyContainers(entity));
    }

    default void populateLinkedProperties(Model model, Long beanId) {
        List<LinkedPropertyContainer> linkedPropertyContainers = getLinkedPropertyContainers(beanId);
        // Update the containers
        if (linkedPropertyContainers != null) {
            linkedPropertyContainers.forEach(container -> {
                container.setParentType(getParentClass().getSimpleName());
                container.setParentId(beanId);
            });
        }
        model.addAttribute("linkedPropertyContainers",
                linkedPropertyContainers == null ? new ArrayList<>() : linkedPropertyContainers);
    }

    /**
     * The containers to be populated on alter (edit/add) page
     * Make sure that what ever you put as the container property name and value should be compatible with the entity
     * as thymeleaf use those information to automatically deserialize the bean when save button is clicked (save
     * method is called) using the form mapper
     *
     * @param entity
     * @return
     */
    default List<GenericPropertyContainer> getPropertyContainers(T entity) {
        List<GenericPropertyContainer> propertyContainers = new ArrayList<>();
        getPropertyMetadata().forEach((property) -> {
            PropertyContainer propertyContainer = new PropertyContainer(property.getName(), property.getTitle());
            propertyContainer.setPropertyValue(BeanContainerUtils.getPropertyValue(entity, property.getName()));
            propertyContainers.add(propertyContainer);
        });
        return propertyContainers;
    }

    default String getListTitle() {
        return StringUtils.pluralize(StringUtils.capitalizeFirstLetters(getViewTitle()));
    }

    default String getView(String viewName) {
        return "views/" + viewName;
    }

    default String getListViewPath() {
        return getView("listView");
    }

    /**
     * The page to add/edit the entity
     *
     * @return
     */
    default String getAlterViewPath() {
        return getView("alterView");
    }

    default String getErrorViewPath() {
        return "error";
    }

    default String getListPath() {
        return String.format("redirect:/%s/list", getRootViewName());
    }

    /**
     * Used to validate the bean before it is saved
     * By default, no validation is handled.
     *
     * @param bean
     * @throws ValidationException
     */
    default void validateBeforeSave(T bean) throws ValidationException {

    }

    /**
     * The list of optional actions that can be added to each ro on the list table
     *
     * @return
     */
    default List<MenuAction> getOptionalActions() {
        return new ArrayList<>();
    }

}
