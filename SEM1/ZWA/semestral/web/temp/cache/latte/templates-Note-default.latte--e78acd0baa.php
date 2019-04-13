<?php
// source: C:\Users\Vojcek\Documents\wamp64\www\semestral\app\presenters/templates/Note/default.latte

use Latte\Runtime as LR;

class Templatee78acd0baa extends Latte\Runtime\Template
{
	public $blocks = [
		'content' => 'blockContent',
	];

	public $blockTypes = [
		'content' => 'html',
	];


	function main()
	{
		extract($this->params);
		if ($this->getParentName()) return get_defined_vars();
		$this->renderBlock('content', get_defined_vars());
		return get_defined_vars();
	}


	function prepare()
	{
		extract($this->params);
		Nette\Bridges\ApplicationLatte\UIRuntime::initialize($this, $this->parentName, $this->blocks);
		
	}


	function blockContent($_args)
	{
		extract($_args);
		?><h2 class="text-center"><?php echo LR\Filters::escapeHtmlText($note->headline) /* line 2 */ ?></h2>
<div class="note-wrapper text-center col-md-6 col-md-offset-3">

    <p class="note-date"><?php echo LR\Filters::escapeHtmlText($note->creation) /* line 5 */ ?></p>
    <div class="note-content">
        <p><?php echo LR\Filters::escapeHtmlText($note->content) /* line 7 */ ?></p>
    </div>

    <div class="note-settings">
        <button class="btn btn-default" data-toggle="modal" data-target="#myModal">Upravit</button>
        <!--<button class="btn btn-default">Vytisknout</button>-->
    </div>

    <div id="myModal" class="" role="dialog">
        <div class="modal-dialog">

            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Úprava poznámky</h4>
                </div>
                <div class="modal-body">
<?php
		$form = $_form = $this->global->formsStack[] = $this->global->uiControl["noteEditForm"];
		?>                    <form id="noteEditForm" method="post" class="note-edit-form"<?php
		echo Nette\Bridges\FormsLatte\Runtime::renderFormBegin(end($this->global->formsStack), array (
		'id' => NULL,
		'method' => NULL,
		'class' => NULL,
		), false) ?>>
                        <input type="hidden" value="<?php echo LR\Filters::escapeHtmlAttr($note_id) /* line 26 */ ?>"<?php
		$_input = end($this->global->formsStack)["note_id"];
		echo $_input->getControlPart()->addAttributes(array (
		'type' => NULL,
		'value' => NULL,
		))->attributes() ?>>
                        <div class="form-group">
                            <label for="headline">Název poznámky</label>

                            <input type="text" class="form-control" id="headline" value="<?php echo LR\Filters::escapeHtmlAttr($note->headline) /* line 30 */ ?>" required maxlength="255"<?php
		$_input = end($this->global->formsStack)["headline"];
		echo $_input->getControlPart()->addAttributes(array (
		'type' => NULL,
		'class' => NULL,
		'id' => NULL,
		'value' => NULL,
		'required' => NULL,
		'maxlength' => NULL,
		))->attributes() ?>>
                        </div>
                        <div class="form-group">
                            <label for="content">Obsah</label>

                            <textarea class="form-control" rows="5" id="content" required maxlength="65535"<?php
		$_input = end($this->global->formsStack)["content"];
		echo $_input->getControlPart()->addAttributes(array (
		'class' => NULL,
		'rows' => NULL,
		'id' => NULL,
		'required' => NULL,
		'maxlength' => NULL,
		))->attributes() ?>><?php echo $_input->getControl()->getHtml() ?></textarea>
                        </div>
                        <div class="form-group">
                            <button class="btn center-block"<?php
		$_input = end($this->global->formsStack)["submit"];
		echo $_input->getControlPart()->addAttributes(array (
		'class' => NULL,
		))->attributes() ?>>Upravit</button>
                        </div>
<?php
		echo Nette\Bridges\FormsLatte\Runtime::renderFormEnd(array_pop($this->global->formsStack), false);
?>                    </form>
                </div>
                <div class="modal-footer">
                    <a class="btn btn-default pull-left" href="<?php echo LR\Filters::escapeHtmlAttr($this->global->uiControl->link("remove", [$note_id])) ?>">Smazat poznámku</a>
                    <button type="button" class="btn btn-default" data-dismiss="modal">Zavřít</button>
                </div>
            </div>

        </div>
    </div>
</div><?php
	}

}
