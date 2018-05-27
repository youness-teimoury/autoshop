# autoshop

Tech stack:
* Java
* JEE
* Spring
* JQuery
* Bootstrap
* Thymeleaf

Tech debts:
* Selenium tests for the CRUD UI functionaries
* More refactor to remove redundant/derivable configurations
* Improve bean validators
* Add date input compatible containers
* Refactor/add especial purpose exception classes
* Bind data containers and register them together. This way, you can make real-time ajax calls to refresh data when a
dependant data-container's value changes (e.g. show only the list of applicable maintenance types when a car is
reselected)
* Add search/filter to views
* Add business logic
* Security and role management
* Improving the UX for exceptions and errors
* Improving the log mechanism and log separation
* Adding a view only page and putting the link on table row (user can navigate to view by clicking on the table row)
* Table sort on column (bootstrap)
* Improving the user interface
* On edit page, save the whole form once (currently oneToMany links are saved through a separate <form>)
* Let user to add links (oneToMany) when creating (add) a new entity
* Address the TODOs in the API
* Refactor reserved words and constants + documentation

Installation & run
git clone https://github.com/youness-teimoury/autoshop
cd autoshop
mvn spring-boot:run