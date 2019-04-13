<?php
// source: C:\Users\Vojcek\Documents\wamp64\www\semestral\app\presenters/templates/Notebook/default.latte

use Latte\Runtime as LR;

class Template46c6e13fda extends Latte\Runtime\Template
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
		if (isset($this->params['note'])) trigger_error('Variable $note overwritten in foreach on line 9');
		Nette\Bridges\ApplicationLatte\UIRuntime::initialize($this, $this->parentName, $this->blocks);
		
	}


	function blockContent($_args)
	{
		extract($_args);
?>
<div class="notebook-wrapper text-center col-md-6 col-md-offset-3">
    <h2>Poznámky  v sešitu <?php echo LR\Filters::escapeHtmlText($headline) /* line 3 */ ?></h2>

    <div class="main-notebooks center-block">
        <button type="button" class="btn btn-default notebook-new-button" data-toggle="modal" data-target="#myModal">Nová poznámka</button>

<?php
		if (!empty($notes)) {
			$iterations = 0;
			foreach ($notes as $note) {
?>
                <div class="main-notebook-wrap">
                    <a href="<?php echo LR\Filters::escapeHtmlAttr($this->global->uiControl->link("Note:default", [$note->id])) ?>">
                        <h3><?php echo LR\Filters::escapeHtmlText($note->headline) /* line 12 */ ?></h3>
                        <p class="note-date"><?php echo LR\Filters::escapeHtmlText($note->creation) /* line 13 */ ?></p>
                        <p><?php echo LR\Filters::escapeHtmlText($note->sneakpeek) /* line 14 */ ?> ...</p>
                    </a>
                </div>
<?php
				$iterations++;
			}
?>

            <div class="pagination btn-group">
<?php
			if (!$paginator->isFirst()) {
				?>                    <a class="btn btn-default" href="<?php echo LR\Filters::escapeHtmlAttr($this->global->uiControl->link("default", [$notebook_id, 1])) ?>">První</a>
                    <a class="btn btn-default" href="<?php echo LR\Filters::escapeHtmlAttr($this->global->uiControl->link("default", [$notebook_id, $paginator->page-1])) ?>">Předchozí</a>
<?php
			}
?>

                <span class="btn btn-default disabled">Stránka <?php echo LR\Filters::escapeHtmlText($paginator->page) /* line 25 */ ?> z <?php
			echo LR\Filters::escapeHtmlText($paginator->pageCount) /* line 25 */ ?></span>

<?php
			if (!$paginator->isLast()) {
				?>                    <a class="btn btn-default" href="<?php echo LR\Filters::escapeHtmlAttr($this->global->uiControl->link("default", [$notebook_id, $paginator->page+1])) ?>">Další</a>
                    <a class="btn btn-default" href="<?php echo LR\Filters::escapeHtmlAttr($this->global->uiControl->link("default", [$notebook_id, $paginator->pageCount])) ?>">Poslední</a>
<?php
			}
?>
            </div>
<?php
		}
		else {
?>
            <h3>Nemáte zatím žádnou poznámku, vytvořte nějakou!</h3>
<?php
		}
?>

        <!--
        <div class="main-notebook-wrap">
            <a href="./note.html">
                <h3>Název Poznámky</h3>
                <p class="note-date">2.2.1990</p>
                <p>Pár prvních slov</p>
            </a>
        </div>
        -->
        <br>
        <button type="button" class="btn btn-danger " data-toggle="modal" data-target="#myModal1">Smazat sešit</button>
    </div>

    <div id="myModal" class="" role="dialog">
        <div class="modal-dialog">

            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Nová poznámka</h4>
                </div>
                <div class="modal-body">
<?php
		$form = $_form = $this->global->formsStack[] = $this->global->uiControl["noteAddForm"];
		?>                    <form id="noteAddForm" method="post" class="note-edit-form"<?php
		echo Nette\Bridges\FormsLatte\Runtime::renderFormBegin(end($this->global->formsStack), array (
		'id' => NULL,
		'method' => NULL,
		'class' => NULL,
		), false) ?>>
                        <input type="hidden" value="<?php echo LR\Filters::escapeHtmlAttr($notebook_id) /* line 60 */ ?>"<?php
		$_input = end($this->global->formsStack)["notebook_id"];
		echo $_input->getControlPart()->addAttributes(array (
		'type' => NULL,
		'value' => NULL,
		))->attributes() ?>>
                        <div class="form-group">
                            <label for="headline">Název poznámky</label>

                            <input type="text" class="form-control" id="headline" required maxlength="255"<?php
		$_input = end($this->global->formsStack)["headline"];
		echo $_input->getControlPart()->addAttributes(array (
		'type' => NULL,
		'class' => NULL,
		'id' => NULL,
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
		))->attributes() ?>>Přidat</button>
                        </div>
<?php
		echo Nette\Bridges\FormsLatte\Runtime::renderFormEnd(array_pop($this->global->formsStack), false);
?>                    </form>
                </div>
            </div>

        </div>
    </div>

    <div id="myModal1" class="" role="dialog">
        <div class="modal-dialog">

            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Smazat sešit</h4>
                </div>
                <div class="modal-body">
                    <form class="note-edit-form">
                        <div class="form-group">
                            <label>Opravdu?</label><br>
                            <a class="btn btn-success" name="submit" href="<?php echo LR\Filters::escapeHtmlAttr($this->global->uiControl->link("Notebook:remove", [$notebook_id])) ?>">Ano</a>
                            <button class="btn btn-danger" data-dismiss="modal">Ne</button>
                        </div>
                    </form>
                </div>
            </div>

        </div>
    </div>
</div><?php
	}

}
