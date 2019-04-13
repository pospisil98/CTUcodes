<?php

namespace App\Presenters;

use App\Model\NotebookManager;
use App\Model\NoteManager;
use Nette;
use Nette\Application\UI\Form;
use Tracy\Debugger;

/**
 * Class HomepagePresenter
 * It is responsible for operations with all notebooks and some other basic stuff.
 *
 * @package App\Presenters
 */
class HomepagePresenter extends BasePresenter
{

    /** @var NotebookManager */
    private $notebookManager;

    /** @var NoteManager */
    private $noteManager;

    /**
     * HomepagePresenter constructor.
     *
     * @param NotebookManager $notebookManager
     * @param NoteManager $noteManager
     */
    public function __construct(NotebookManager $notebookManager, NoteManager $noteManager) {
        $this->notebookManager = $notebookManager;
        $this->noteManager = $noteManager;
    }

    /**
     * Default action - just checks login.
     *
     * @param int $page Paginator page
     */
    public function actionDefault($page = 1) {
        if(!$this->user->isLoggedIn()) {
            $this->redirect('Sign:in');
        }
    }

    /**
     * Renders default template - list of all user notebooks.
     *
     * @param int $page Paginator page
     */
	public function renderDefault($page = 1) {
        $notebookCount = $this->notebookManager->getUserNotebooksCount($this->user->id);
        //Debugger::barDump($notebookCount[0], "CountN");

        $paginator = new Nette\Utils\Paginator;
        $paginator->setItemCount($notebookCount[0]);
        $paginator->setItemsPerPage(3);
        $paginator->setPage($page);

        $this->template->notebooks = $this->notebookManager->getAllUserNotebooks($this->user->id, $paginator->getLength(), $paginator->getOffset());
        $this->template->paginator = $paginator;
	}

    /**
     * Creates quickNotes form.
     *
     * @return Form QuickNote form with headline and content
     */
    public function createComponentQuickNoteForm() {
        $form = new Form();

        $form->addText('headline')
            ->setRequired("Toto pole je povinné.")
            ->addRule(Form::MAX_LENGTH, "Nadpis může mít maximálně %d znaků", 60);
        $form->addTextarea('content')
            ->setRequired("Toto pole je povinné.")
            ->addRule(Form::MAX_LENGTH, "Obsah může mít maximálně %d znaků", 65535);
        $form->addSubmit('submit');

        $form->onSuccess[] = array($this, 'quickNoteFormSucceeded');

        return $form;
    }

    /**
     * Creates notebookAdd form.
     *
     * @return Form NotebookAdd form with headline and description
     */
	public function createComponentNotebookAddForm() {
        $form = new Form();

        $form->addText('headline')
            ->setRequired("Toto pole je povinné.")
            ->addRule(Form::MAX_LENGTH, "Nadpis může mít maximálně %d znaků", 60);
        $form->addTextarea('description')
            ->setRequired("Toto pole je povinné.")
            ->addRule(Form::MAX_LENGTH, "Popis může mít maximálně %d znaků", 255);
        $form->addSubmit('submit');

        $form->onSuccess[] = array($this, 'notebookAddFormSucceeded');

        return $form;
    }

    /**
     * Function called after successful quickNoteForm submit. It is responsible for check of QuickNotes Notebook existance and
     * int's creation when necessary and adding the quick note.
     *
     * @param $form Form QuickNote form
     * @param $values array Array of values returned by QuickNote form
     */
    public function quickNoteFormSucceeded($form, $values) {
        if(($quick_id = $this->notebookManager->checkQuickNotesExistence($this->user->id)) == false) {
            $quick_id = $this->notebookManager->createQuickNotes($this->user->id);
            $this->flashMessage("Vytvořen sešit pro rychlé poznámky!", 'success');
        }

        Debugger::barDump($quick_id ,"ID FAST");

        if(strlen($values['headline']) > 255 or $values['headline'] == "") {
            $this->flashMessage("Nesprávná délka nadpisu", 'danger');
            $this->template->headline = $values['headline'];
            $this->template->content = $values['content'];
        } else if(strlen($values['content']) > 65535 or $values['content'] == "") {
            $this->flashMessage("Nesprávná délka obsahu", 'danger');
            $this->template->headline = $values['headline'];
            $this->template->content = $values['content'];
        } else {
            $this->noteManager->addNote($values['headline'], $values['content'], $quick_id);

            $this->flashMessage("Poznámka přidána!", 'success');
            $this->redirect("default");
        }
    }

    /**
     * Function called after successful notebookAddForm submit. It is responsible for adding notebook for user.
     *
     * @param $form Form NotebookAdd form
     * @param $values array Array of values returned by NotebookAdd form
     */
    public function notebookAddFormSucceeded($form, $values) {
        if(strlen($values['headline']) > 60 or $values['headline'] == "") {
            $this->flashMessage("Nesprávná délka nadpisu", 'danger');
            $this->template->notebookHeadline = $values['headline'];
            $this->template->description = $values['description'];
            $this->template->modalToggle = True;
            $this->template->modalName = "#myModal";
        } else if(strlen($values['description']) > 255 or $values['description'] == "") {
            $this->flashMessage("Nesprávná délka obsahu", 'danger');
            $this->template->notebookHeadline = $values['headline'];
            $this->template->description = $values['description'];
            $this->template->modalToggle = True;
            $this->template->modalName = "#myModal";
        } else {
            try {
                $this->notebookManager->addNotebook($values['headline'], $values['description'], $this->user->id);

                $this->template->notebookHeadline = "";
                $this->template->description = "";

                $this->flashMessage("Sešit přidán!", 'success');
                $this->redirect("default");
            } catch (Nette\UnexpectedValueException $e) {
                $this->template->notebookHeadline = $values['headline'];
                $this->template->description = $values['description'];
                $this->template->modalToggle = True;
                $this->template->modalName = "#myModal";

                $this->flashMessage($e->getMessage(), 'danger');
            }
        }
    }
}
