<?php
/**
 * Created by PhpStorm.
 * User: Vojcek
 * Date: 24/12/2017
 * Time: 00:41
 */

namespace App\Model;

use App\Model\NoteManager;
use Nette,
    Tracy\Debugger;

/**
 * Notebooks management
 *
 * @package App\Model
 */
class NotebookManager
{
    use Nette\SmartObject;

    /** @var Nette\Database\Context */
    private $database;

    /** @var NoteManager */
    private $noteManager;

    const QUICK_NOTES_HEADLINE = "Rychlé poznámky";
    const QUICK_NOTES_DESCRIPTION = "Sešit pro rychlé poznámky";

    /**
     * NotebookManager constructor.
     *
     * @param Nette\Database\Connection $database Database connection
     * @param \App\Model\NoteManager $noteManager Note management
     */
    public function __construct(Nette\Database\Connection $database, NoteManager $noteManager)
    {
        $this->database = $database;
        $this->noteManager = $noteManager;
    }

    /**
     * Adds notebook into database.
     *
     * @param $headline string Headline of notebook
     * @param $description string Description of notebook
     * @param $ownerId int ID of owner
     */
    public function addNotebook($headline, $description, $ownerId)
    {
        $row = $this->database->query("
            SELECT id
            FROM notebooks
            WHERE headline = ? AND owner_id = ?
        ", $headline, $ownerId)->fetch();

        //Debugger::barDump($row);

        if ($row) {
            throw new Nette\UnexpectedValueException("Sešit s daným názvem již vlastníte!");
        } else {
            $this->database->query("
            INSERT INTO notebooks (id, headline, owner_id, description)
            VALUES (NULL , ?, ?, ?) 
        ", $headline, $ownerId, $description);
        }

    }

    /**
     * Removes notebook from database.
     *
     * @param $id int ID of notebook to be removed
     */
    public function removeNotebook($id)
    {
        $ids = $this->getAllNotebookNotesIDs($id);

        foreach ($ids as $note) {
            $this->noteManager->removeNote($note['id']);
        }

        $this->database->query("
            DELETE FROM notebooks
            WHERE id = ?
        ", $id);
    }

    /**
     * Returns number of user owned notebooks (for paginator mostly).
     *
     * @param $id int ID of user we want to know number of notebooks
     * @return int Number of owned notebooks
     */
    public function getUserNotebooksCount($id)
    {
        $count = $this->database->query("
            SELECT COUNT(id)
            FROM notebooks
            WHERE owner_id = ?

        ", $id);

        return $count->fetch();
    }

    /**
     * Returns array with all notebooks of user.
     *
     * @param $id int ID of user
     * @param $limit int Paginator stuff
     * @param $offset int Paginator stuff
     * @return array Array of all notebook
     */
    public function getAllUserNotebooks($id, $limit, $offset)
    {
        $notebooks = $this->database->query("
            SELECT *
            FROM notebooks
            WHERE owner_id = ?
            LIMIT ?
            OFFSET ?
        ", $id, $limit, $offset);

        return $notebooks->fetchAll();
    }

    /**
     * Returns owner ID of given notebook.
     *
     * @param $id int ID of notebook
     * @return int ID of user owning given notebook
     */
    public function getNotebookOwnerId($id)
    {
        Debugger::barDump($id, "IDčko");
        $res = $this->database->query("SELECT owner_id
            FROM notebooks
            WHERE id = ?", $id);

        Debugger::barDump($res, "SELECT ID");

        return $res->fetch()['owner_id'];
    }

    /**
     * Reurns all notes in given notebook.
     *
     * @param $id int ID of notebooks
     * @return array All notes in given notebook.
     */
    public function getAllNotebookNotes($id)
    {
        $notesIds = $this->getAllNotebookNotesIDs($id);

        $notes = array();
        foreach ($notesIds as $note) {
            $notes[] = $this->noteManager->getNote($note['id']);
        }

        return $notes;
    }

    /**
     * Returns count of notes in notebooks (mainly for paginator).
     *
     * @param $id int ID of notebook we're interested in
     * @return int Number of notes
     */
    public function getNotesCountInNotebook($id)
    {
        return count($this->getAllNotebookNotesIDs($id));
    }

    /**
     * Returns headline of given notebook.
     *
     * @param $id int ID of notebook
     * @return string Headline of given notebook
     */
    public function getNotebookHeadline($id) {
        $row = $this->database->query("
            SELECT headline 
            FROM notebooks
            WHERE id = ?
       ", $id);

        return $row->fetch();
    }

    /**
     * Returns notebook id of note.
     *
     * @param $note_id int ID of note
     * @return mixed
     */
    public function getNotebookIdForNote($note_id) {
        return $this->noteManager->getNotebookId($note_id);
    }

    /**
     * Checks existance of notebook for quick notes.
     *
     * @param $id int ID of user we are interested in
     * @return bool|int False or ID of quick notes notebook
     */
    public function checkQuickNotesExistence($id) {
        $row = $this->database->query("
            SELECT id
            FROM notebooks
            WHERE owner_id = ? AND headline = ?
        ", $id, self::QUICK_NOTES_HEADLINE)->fetch();

        if(!$row) {
            return false;
        } else {
            return $row['id'];
        }
    }

    /**
     * Creates quick notes notebook for given user.
     *
     * @param $id int ID of user we want to create quick notes notebook.
     * @return int ID of created quick notes notebook
     */
    public function createQuickNotes($id) {
        $this->database->query("
            INSERT INTO notebooks (id, headline, owner_id, description)
            VALUES (NULL , ?, ?, ?) 
        ", self::QUICK_NOTES_HEADLINE, $id, self::QUICK_NOTES_DESCRIPTION);

        return $this->database->getInsertId('notebooks');
    }

    /**
     * Returns IDs of all notes in given notebook.
     *
     * @param $id int ID of notebook we are interested to get notes IDs
     * @return array Array of notes IDs
     */
    private function getAllNotebookNotesIDs($id)
    {
        $row = $this->database->query("
            SELECT id
            FROM notes
            WHERE notebook_id = ? 
        ", $id);

        return $row->fetchAll();
    }
}
