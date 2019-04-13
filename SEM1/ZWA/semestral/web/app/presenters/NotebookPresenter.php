<?php

namespace App\Presenters;

use App\Model\NotebookManager;
use App\Model\NoteManager;
use Nette;
use Nette\Application\UI\Form;
use Tracy\Debugger;

/**
 * Class NotebookPresenter
 * It is responsible for notebook oriented tasks (display, remove, add)
 *
 * @package App\Presenters
 */
class NotebookPresenter extends BasePresenter
{
    /** @var NotebookManager */
    private $notebookManager;

    /** @var NoteManager */
    private $noteManager;

    /**
     * NotebookPresenter constructor.
     *
     * @param NotebookManager $notebookManager Model working with notebooks table
     * @param NoteManager $noteManager Model working with notes table
     */
    public function __construct(NotebookManager $notebookManager, NoteManager $noteManager) {
        $this->notebookManager = $notebookManager;
        $this->noteManager = $noteManager;
    }

    /**
     * Performs actions before rendering the notebook view.
     *
     * @param int $id ID of notebook to be displayed
     * @param int $page Paginator page
     */
    public function actionDefault($id, $page=1) {
        $this->logCheck();

        $notebookOwner = $this->notebookManager->getNotebookOwnerId($id);
        if($notebookOwner != $this->user->id) {
            $this->flashMessage("Nemáte oprávnění prohlížet tento sešit", "warning");
            $this->redirect("Homepage:default");
        }
    }

    /**
     * Renders default notebook page based on
     *
     * @param int $id ID of notebook to be displayed
     * @param int $page Paginator page
     */
    public function renderDefault($id, $page=1) {
        $notesCount = $this->notebookManager->getNotesCountInNotebook($id);

        $paginator = new Nette\Utils\Paginator;
        $paginator->setItemCount($notesCount);
        $paginator->setItemsPerPage(3);
        $paginator->setPage($page);

        $notes = $this->noteManager->getAllNotebookNotes($id, $paginator->getLength(), $paginator->getOffset());
        $notes = $this->createSneakpeekForNotes($notes);

        $this->template->headline = $this->notebookManager->getNotebookHeadline($id)['headline'];
        $this->template->notes = $notes;
        $this->template->paginator = $paginator;
        $this->template->notebook_id = $id;
    }

    /**
     * Function responsible for removing note. It checks ownership and so on.
     *
     * @param int $id ID of note to be removed
     */
    public function actionRemove($id) {
        $this->logCheck();

        $notebookOwner = $this->notebookManager->getNotebookOwnerId($id);
        Debugger::barDump($notebookOwner, "vlastník");
        if($notebookOwner != $this->user->id) {
            $this->flashMessage("Nemáte oprávnění smazat tento sešit", "warning");
            $this->redirect("Homepage:default");
        }

        $this->notebookManager->removeNotebook($id);

        $this->flashMessage("Sešit byl úspěšně smazán", "success");
        $this->redirect("Homepage:Default");
    }

    /**
     * Function creating the NoteAdd form
     *
     * @return Form NoteAdd form with hiddne notebookID and headline + content inputs
     */
    public function createComponentNoteAddForm() {
        $form = new Form();

        $form->addHidden('notebook_id');
        $form->addText('headline')
            ->setRequired("Toto pole je povinné.")
            ->addRule(Form::MAX_LENGTH, "Nadpis může mít maximálně %d znaků", 60);
        $form->addTextarea('content')
            ->setRequired("Toto pole je povinné.")
            ->addRule(Form::MAX_LENGTH, "Popis může mít maximálně %d znaků", 65535);
        $form->addSubmit('submit');

        $form->onSuccess[] = array($this, 'noteAddFormSucceeded');

        return $form;
    }

    /**
     * Function called after successful noteAdd submit. It is resposbile for
     * note addition and notifying the user.
     *
     * @param $form
     * @param $values
     */
    public function noteAddFormSucceeded($form, $values) {
        //Debugger::barDump($values, "Add note values");

         $id = $this->noteManager->addNote($values['headline'], $values['content'], $values['notebook_id']);

        $this->flashMessage("Poznámka byla úspěšně přidána", "success");
        $this->redirect("Note:default", array("id" => $id));
    }

    /**
     * Function that creates preview of given notes - it returns x first words.
     *
     * @param $notes array Notes to make preview to
     * @return array Finished previews
     */
    private function createSneakpeekForNotes($notes) {
        foreach ($notes as &$note) {
            $pieces = explode(" ", $note["content"]);
            $sneakpeek = implode(" ", array_splice($pieces, 0, 5));
            $note['sneakpeek'] = $sneakpeek;
        }

        return $notes;
    }
}
