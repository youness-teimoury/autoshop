// This is to link/add a new entity (child) to the owner entity
// This script registers a event listener for each form to let user to add an item (child entity) to the table (list of
// children in owner entity) while sending an AJax call to the server to update the server too

// Return the LinkTable in the form
function getLinkTable($form) {
    // Using the linkedPropertyContainer's propertyName on the form's ID, returns the link table ID
    var linkTableName = 'linkTable_' + $form.attr('id').split('_')[1];
    return $("#" + linkTableName);
}

// Return the Confirmation element (as a JQuery object) in the form
function getFormPostConfirmation($form) {
    // Using the linkedPropertyContainer's propertyName on the form's ID, returns the post confirmation element ID
    return 'formPostConfirmation_' + $form.attr('id').split('_')[1];
}


(function ($) {
    // Get all the forms that support Async post. The id of such forms start with asyncPostForms by our convention.
    var asyncPostForms = $('form[id^="ayncPostForm_"]');
    // Iterate through them and set the submit function on each
    asyncPostForms.on('submit', function (e) {
        debugger;
        e.preventDefault();
        // e.target is the form which submit has happened on. T
        // convert DOM Form element to JQuery object
        $form = $(e.target);

        var $linkTable = getLinkTable($form);
        var $formPostConfirmationElement = getFormPostConfirmation($form);

        $.ajax({
            url: $form.attr('action'),
            type: 'post',
            data: $form.serialize(),
            success: function (response) {
                debugger;
                if (typeof $(response)[0].error !== "undefined" && !$(response)[0].error) {
                    $linkTable.append('<tr><td>' + $(response)[0].entity + '</td></tr>');
                } else if (typeof $(response)[0].error !== "undefined" && $(response)[0].error) {
                    $formPostConfirmationElement.text($(response)[0].message)
                } else {
                    // if the response contains any errors, replace the form
                    $form.replaceWith(response);
                }
            },
            error: function (response) {
                // if the response contains any errors, replace the form
                $form.replaceWith(response);
            }
        });
    });
}(jQuery));