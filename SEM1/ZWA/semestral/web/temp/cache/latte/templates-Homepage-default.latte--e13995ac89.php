<?php
// source: C:\Users\Vojcek\Documents\wamp64\www\semestral\app\presenters/templates/Homepage/default.latte

use Latte\Runtime as LR;

class Templatee13995ac89 extends Latte\Runtime\Template
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
		if (isset($this->params['notebook'])) trigger_error('Variable $notebook overwritten in foreach on line 10');
		Nette\Bridges\ApplicationLatte\UIRuntime::initialize($this, $this->parentName, $this->blocks);
		
	}


	function blockContent($_args)
	{
		extract($_args);
?>
<div class="main-wrapper text-center col-md-6 col-md-offset-3">
	<h2>Vaše poznámkové sešity</h2>

	<div class="main-notebooks center-block">
		<button type="button" class="btn btn-default notebook-new-button" data-toggle="modal" data-target="#myModal">Nový sešit</button>
		<button type="button" class="btn btn-default notebook-new-button" data-toggle="modal" data-target="#myModal1">Rychlá poznámka</button>

<?php
		if (!empty($notebooks)) {
			$iterations = 0;
			foreach ($notebooks as $notebook) {
?>
		<div class="main-notebook-wrap">
			<a href="<?php echo LR\Filters::escapeHtmlAttr($this->global->uiControl->link("Notebook:default", [$notebook->id])) ?>">
				<h3><?php echo LR\Filters::escapeHtmlText($notebook->headline) /* line 13 */ ?></h3>
				<p><?php echo LR\Filters::escapeHtmlText($notebook->description) /* line 14 */ ?></p>
			</a>
		</div>
<?php
				$iterations++;
			}
?>

		<div class="pagination btn-group">
<?php
			if (!$paginator->isFirst()) {
				?>				<a class="btn btn-default" href="<?php echo LR\Filters::escapeHtmlAttr($this->global->uiControl->link("default", [1])) ?>">První</a>
				<a class="btn btn-default" href="<?php echo LR\Filters::escapeHtmlAttr($this->global->uiControl->link("default", [$paginator->page-1])) ?>">Předchozí</a>
<?php
			}
?>

			<span class="btn btn-default disabled">Stránka <?php echo LR\Filters::escapeHtmlText($paginator->page) /* line 25 */ ?> z <?php
			echo LR\Filters::escapeHtmlText($paginator->pageCount) /* line 25 */ ?></span>

<?php
			if (!$paginator->isLast()) {
				?>				<a class="btn btn-default" href="<?php echo LR\Filters::escapeHtmlAttr($this->global->uiControl->link("default", [$paginator->page+1])) ?>">Další</a>
				<a class="btn btn-default" href="<?php echo LR\Filters::escapeHtmlAttr($this->global->uiControl->link("default", [$paginator->pageCount])) ?>">Poslední</a>
<?php
			}
?>
		</div>
<?php
		}
		else {
?>
			<h3>Nemáte zatím žádný sešit, vytvořte si nějaký!</h3>
<?php
		}
?>

		<div id="myModal" class="" role="dialog">
			<div class="modal-dialog">

				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<h4 class="modal-title">Nový sešit</h4>
					</div>
					<div class="modal-body">
<?php
		$form = $_form = $this->global->formsStack[] = $this->global->uiControl["notebookAddForm"];
		?>						<form id="notebookAddForm" method="post" class="note-edit-form"<?php
		echo Nette\Bridges\FormsLatte\Runtime::renderFormBegin(end($this->global->formsStack), array (
		'id' => NULL,
		'method' => NULL,
		'class' => NULL,
		), false) ?>>
							<div class="form-group">
								<label for="headline">Název sešitu</label>

								<input type="text" class="form-control" id="headline" required maxlength="60" value="<?php
		if (isset($notebookHeadline)) {
			echo LR\Filters::escapeHtmlAttr($notebookHeadline) /* line 50 */;
		}
		?>"<?php
		$_input = end($this->global->formsStack)["headline"];
		echo $_input->getControlPart()->addAttributes(array (
		'type' => NULL,
		'class' => NULL,
		'id' => NULL,
		'required' => NULL,
		'maxlength' => NULL,
		'value' => NULL,
		))->attributes() ?>>
							</div>
							<div class="form-group">
								<label for="description">Stručný popis</label>

								<textarea class="form-control" rows="5" id="description" required maxlength="255"<?php
		$_input = end($this->global->formsStack)["description"];
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
?>						</form>
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
						<h4 class="modal-title">Rychlá poznámka</h4>
					</div>
					<div class="modal-body">
<?php
		$form = $_form = $this->global->formsStack[] = $this->global->uiControl["quickNoteForm"];
		?>						<form id="quickNoteForm" method="post" class="note-edit-form"<?php
		echo Nette\Bridges\FormsLatte\Runtime::renderFormBegin(end($this->global->formsStack), array (
		'id' => NULL,
		'method' => NULL,
		'class' => NULL,
		), false) ?>>
							<div class="form-group">
								<label for="headline">Název poznámky</label>

								<input type="text" class="form-control" id="headline" value="<?php
		if (isset($headline)) {
			echo LR\Filters::escapeHtmlAttr($headline) /* line 79 */;
		}
		?>" required maxlength="60"<?php
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
		))->attributes() ?>>Přidat</button>
							</div>
<?php
		echo Nette\Bridges\FormsLatte\Runtime::renderFormEnd(array_pop($this->global->formsStack), false);
?>						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</div><?php
	}

}
