$( document ).ready(function() {
    console.log( "ready!" );

    $('#notebookAddForm').preventDoubleSubmission();
    $('#quickNoteForm').preventDoubleSubmission();
    $('#signUpForm').preventDoubleSubmission();
    $('#noteEditForm').preventDoubleSubmission();
    $('#noteAddForm').preventDoubleSubmission();

    $('div[id^="myModal"]').addClass("modal fade");
});

// jQuery plugin to prevent double submission of forms
jQuery.fn.preventDoubleSubmission = function() {
    $(this).on('submit',function(e){
        console.log("PREVENTED");
        var $form = $(this);

        if ($form.data('submitted') === true) {
            // Previously submitted - don't submit again
            e.preventDefault();
        } else {
            // Mark it so that the next submit can be ignored
            $form.data('submitted', true);
        }
    });

    // Keep chainability
    return this;
};
