<?php

namespace App\Presenters;

use Nette;


/**
 * Base presenter for all application presenters.
 *
 * This line is for phpStorm autocompletion and suggestions
 * @property-read \Nette\Bridges\ApplicationLatte\Template|\stdClass $template
 */
abstract class BasePresenter extends Nette\Application\UI\Presenter
{
    /**
     *  Function responsible for checking whether the user is logged in or not.
     */
    protected function logCheck() {
        if(!$this->user->isLoggedIn()) {
            $this->flashMessage("Nemáte oprávnění navštěvovat tuto stánku, přihlašte se prosím.", "warning");
            $this->redirect("Sign:in");
        }
    }
}
