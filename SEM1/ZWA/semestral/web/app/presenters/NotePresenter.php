<?php

namespace App\Presenters;

use App\Model\NotebookManager;
use App\Model\NoteManager;
use Nette;
use Nette\Application\UI\Form;
use Tracy\Debugger;

/**
 * Class NotePresenter
 * It is responsible for tasks connected with notes (display, edit).
 *
 * @package App\Presenters
 */
class NotePresenter extends BasePresenter
{
    /** @var NotebookManager */
    private $notebookManager;

    /** @var NoteManager */
    private $noteManager;

    /**
     * NotePresenter constructor.
     *
     * @param NotebookManager $notebookManager Model working with notebooks table
     * @param NoteManager $noteManager Model working with notes table
     */
    public function __construct(NotebookManager $notebookManager, NoteManager $noteManager) {
        $this->notebookManager = $notebookManager;
        $this->noteManager = $noteManager;
    }

    /**
     * Performs actions before rendering the note view.
     *
     * @param $id int ID of note to be displayed
     */
    public function actionDefault($id) {
        $this->logCheck();

        if(!$this->noteManager->checkOwnership($id, $this->user->id)) {
            $this->flashMessage("Nemáte oprávnění prohlížet tuto poznámku", "warning");
            $this->redirect("Homepage:default");
        }
    }

    /**
     * Renders note view with corresponding note.
     *
     * @param $id int ID of note to be displayed
     */
    public function renderDefault($id) {
        $this->template->note = $this->noteManager->getNote($id);
        $this->template->note_id = $id;
        $this->template->notebookID = $this->noteManager->getNotebookId($id);
    }

    /**
     * Action responsible for removing the note from database.
     *
     * @param $id int ID of note to be removed
     */
    public function actionRemove($id) {
        if(!$this->noteManager->checkOwnership($id, $this->user->id)) {
            $this->flashMessage("Nemáte oprávnění mazat tuto poznámku", "warning");
            $this->redirect("Homepage:default");
        }

        $notebook_id = $this->notebookManager->getNotebookIdForNote($id);
        $this->noteManager->removeNote($id);

        Debugger::barDump($notebook_id, 'ntbID');

        $this->flashMessage("Poznámka smazána", "success");
        $this->redirect("Notebook:default", array("id" => $notebook_id));
    }

    /**
     * Function creating the NoteEdit form.
     *
     * @return Form NoteEdit form with hidden noteID, headline and content of note
     */
    public function createComponentNoteEditForm() {
        $form = new Form();

        $form->addHidden('note_id');
        $form->addText('headline')
            ->setRequired("Toto pole je povinné.")
            ->addRule(Form::MAX_LENGTH, "Nadpis může mít maximálně %d znaků", 60);
        $form->addTextarea('content')
            ->setRequired("Toto pole je povinné.")
            ->addRule(Form::MAX_LENGTH, "Popis může mít maximálně %d znaků", 65535);
        $form->addSubmit('submit');

        $form->onSuccess[] = array($this, 'noteEditFormSucceeded');

        return $form;
    }

    /**
     * Function called after successful noteEdit submit. It is responsible for performing
     * changes in the database.
     *
     * @param $form
     * @param $values
     */
    public function noteEditFormSucceeded($form, $values) {
        Debugger::barDump($values,"HODNOTYY");

        $this->noteManager->editNote($values['note_id'], $values['headline'], $values['content']);

        $this->flashMessage("Poznámka upravena!", "success");
        $this->redirect("Note:default", array("id" => $values['note_id']));
    }
}
