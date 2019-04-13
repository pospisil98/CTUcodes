<?php
/**
 * Created by PhpStorm.
 * User: Vojcek
 * Date: 24/12/2017
 * Time: 00:41
 */

namespace App\Model;

use Nette,
    Tracy\Debugger;

/**
 * Notes management
 *
 * @package App\Model
 */
class NoteManager
{
    use Nette\SmartObject;

    /**
     * @var Nette\Database\Context
     */
    private $database;


    /**
     * NoteManager constructor.
     *
     * @param Nette\Database\Connection $database Database connection
     */
    public function __construct(Nette\Database\Connection $database)
    {
        $this->database = $database;
    }

    /**
     * Returns everything connected with note of given ID.
     *
     * @param $id int ID of note we want to know
     * @return array Everything with note
     */
    public function getNote($id) {
        $note = $this->database->query("
            SELECT *
            FROM notes
            WHERE id = ?
        ", $id);

        return $note->fetch();
    }

    /**
     * Inserts note with given headline and content into notebook.
     *
     * @param $headline string Headline of note
     * @param $content string Content of note
     * @param $notebook_id int ID of notebook to be note inserted in
     * @return int ID of inserted note
     */
    public function addNote($headline, $content, $notebook_id)
    {
        $this->database->query("
            INSERT INTO notes (id, notebook_id, headline, content)
            VALUES (NULL , ?, ?, ?) 
        ", $notebook_id, $headline, $content);

        return $this->database->getInsertId("notes");
    }

    /**
     * Removes note from database.
     *
     * @param $id int ID of note to be removed
     */
    public function removeNote($id) {
        $this->database->query("
            DELETE FROM notes
            WHERE id = ?
        ", $id);
    }

    /**
     * Change notes headline and content.
     *
     * @param $id int ID of note to be changed
     * @param $headline string
     * @param $content string
     */
    public function editNote($id, $headline, $content) {
        $this->database->query("
            UPDATE notes
            SET headline = ?, content = ?
            WHERE id = ?
        ", $headline, $content, $id);
    }

    /**
     * Returns all notes in given notebook.
     *
     * @param $notebook_id int ID of notebook
     * @param $limit int Paginator stuff
     * @param $offset int Paginator stuff
     * @return array All notebook notes
     */
    public function getAllNotebookNotes($notebook_id, $limit, $offset) {
        $notes = $this->database->query("
            SELECT *
            FROM notes
            WHERE notebook_id = ?
            LIMIT ?
            OFFSET ?
        ", $notebook_id, $limit, $offset);

        return $notes->fetchAll();
    }

    /**
     * Checks whether given user owns given note or not.
     *
     * @param $note_id int ID of note
     * @param $user_id int ID of user we want to check
     * @return bool Boolean whether user is owner of note or not
     */
    public function checkOwnership($note_id, $user_id) {
        //Debugger::barDump($note_id, "note id");
        //Debugger::barDump($user_id, "user id");

        $notebook_id = $this->database->query("
            SELECT notebook_id
            FROM notes
            WHERE id = ?
        ", $note_id)->fetch()['notebook_id'];

        //Debugger::barDump($notebook_id, "NB ID");

        $notebookOwner = $this->database->query("
            SELECT owner_id
            FROM notebooks
            WHERE id = ?
        ", $notebook_id)->fetch()['owner_id'];

        //Debugger::barDump($notebookOwner, "ownID");

        if($user_id == $notebookOwner) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns notebook_id in which is note stored.
     *
     * @param $note_id int ID of note which notebook we want to know
     * @return int ID of notebook
     */
    public function getNotebookId($note_id) {
        $row = $this->database->query("
            SELECT notebook_id
            FROM notes
            WHERE id = ?
        ", $note_id);

        return $row->fetch()['notebook_id'];
    }
}