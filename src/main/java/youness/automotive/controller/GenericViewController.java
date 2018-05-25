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

import java.security.InvalidParameterException;
import java.util.*;
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

    final Logger logger = LoggerFactory.getLogger(GenericViewController.class);

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
     * The enntity name to be previewed on  create/edit/list views
     *
     * @return
     */
    String getEntityName();

    /**
     * The ordered (by insert) map of column/property names and their captions that can be viewed on the list table's view
     * Also, these properties are used to build the Add or Edit (header part) view
     *
     * @return TODO make it through reflection/annotation as the default behaviour?
     */
    LinkedHashMap<String, String> getPropertyMetadata();

    /**
     * The linked property containers to be previewed on the view
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
     * @throws IllegalArgumentException when link with unique name is not recognized
     */
    String handleSaveDataLinkRequest(DataLinkRequestBean bean, String linkUniqueName) throws IllegalArgumentException;

    /**
     * The method to handle view responsible to list the entities in a table
     *
     * @param model
     * @return
     */
    @RequestMapping("/list")
    default String list(Model model) {
        List<T> entities = getRepository().findAll();
        List<BeanContainer> beanContainers =
                BeanContainerUtils.createBeanContainers(entities, getPropertyNames(), null);

        // Used to populate row values in the view's table
        model.addAttribute("list", beanContainers);
        // Used to populate column names in the view's table
        model.addAttribute("entityPropertyNames", getColumnCaptions());
        // TODO make above attributes into one

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

        populatePageMetadata(model, "Add " + getEntityName());
        populatePropertyMetadata(model, entity);
        return getAlterViewPath();
    }

    @RequestMapping(value = "/edit")
    default String edit(@RequestParam("id") Long beanId, Model model) {
        Optional<T> optional = getRepository().findById(beanId);
        if (optional.isPresent()) {
            T entity = optional.get();
            model.addAttribute("bean", entity);
            populatePropertyMetadata(model, entity);

            List<LinkedPropertyContainer> linkedPropertyContainers = getLinkedPropertyContainers(beanId);
            linkedPropertyContainers.forEach(container -> {
                container.setParentType(getParentClass().getSimpleName());
                container.setParentId(beanId);
            });
            model.addAttribute("linkedPropertyContainers",
                    linkedPropertyContainers == null ? new ArrayList<>() : linkedPropertyContainers);

            populatePageMetadata(model, "Edit " + getEntityName());
            return getAlterViewPath();
        } else {
            return getErrorViewPath();
        }

    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    default String save(T bean) {
        if (bean.getId() != null) { // existing entity is updated
            Optional<T> optionalEntity = getRepository().findById(bean.getId());
            if (optionalEntity.isPresent()) {
                T entity = optionalEntity.get();

                // getPropertyMetadata() is used in populatePropertyMetadata() to populate the edit/add view. Therefore,
                // the input parameter input bean should be only considered for those properties returned by getPropertyMetadata()
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
            String responseString = handleSaveDataLinkRequest(bean, linkUniqueName);

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
        // though the returned set is guaranteed to maintain insertion order, we convert to list just to emphasize
        // the order fact
        return new ArrayList<>(getPropertyMetadata().keySet());
    }

    default Collection<String> getColumnCaptions() {
        return getPropertyMetadata().values();
    }

    default void populatePageMetadata(Model model, String title) {
        model.addAttribute("pageMetadata", new PageMetaData(title));
    }

    default void populatePropertyMetadata(Model model, T entity) {
        List<PropertyContainer> propertyContainers = new ArrayList<>();
        getPropertyMetadata().forEach((propertyName, propertyCaption) -> {
            PropertyContainer propertyContainer = new PropertyContainer(propertyName, propertyCaption);
            propertyContainer.setPropertyValue(BeanContainerUtils.getPropertyValue(entity, propertyName));
            propertyContainers.add(propertyContainer);
        });
        model.addAttribute("propertyContainers", propertyContainers);
    }

    default String getListTitle() {
        return StringUtils.pluralize(StringUtils.capitalizeFirstLetters(getEntityName()));
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
}
