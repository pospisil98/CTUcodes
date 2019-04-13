<?php
// source: C:\Users\Vojcek\Documents\wamp64\www\semestral\app\presenters/templates/@layout.latte

use Latte\Runtime as LR;

class Template7041a71fa4 extends Latte\Runtime\Template
{
	public $blocks = [
		'head' => 'blockHead',
		'scripts' => 'blockScripts',
	];

	public $blockTypes = [
		'head' => 'html',
		'scripts' => 'html',
	];


	function main()
	{
		extract($this->params);
?>
<!DOCTYPE html>
<html lang="cs">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">

	<title>NOTE.IT</title>

	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="<?php echo LR\Filters::escapeHtmlAttr(LR\Filters::safeUrl($basePath)) /* line 15 */ ?>/css/style.css">
	<link rel="stylesheet" href="<?php echo LR\Filters::escapeHtmlAttr(LR\Filters::safeUrl($basePath)) /* line 16 */ ?>/css/bootstrap.min.css">
	<link rel="stylesheet" href="<?php echo LR\Filters::escapeHtmlAttr(LR\Filters::safeUrl($basePath)) /* line 17 */ ?>/css/bootstrap-theme.min.css">

	<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
	<?php
		if ($this->getParentName()) return get_defined_vars();
		$this->renderBlock('head', get_defined_vars());
?>
</head>

<body>
<nav class="navbar navbar-default">
	<div class="container-fluid">
		<div class="navbar-header">
			<a class="navbar-brand" href="<?php echo LR\Filters::escapeHtmlAttr($this->global->uiControl->link("Homepage:")) ?>">NOTE.IT</a>
		</div>
<?php
		if ($user->loggedIn) {
?>
		<ul class="nav navbar-nav">
			<li><a href="<?php echo LR\Filters::escapeHtmlAttr($this->global->uiControl->link("Homepage:")) ?>">Přehled sešitů</a></li>


<?php
			if (isset($notebookID)) {
				?>			<li><a href="<?php echo LR\Filters::escapeHtmlAttr($this->global->uiControl->link("Notebook:default", [$notebookID])) ?>">Zpět na sešit</a></li>
<?php
			}
?>
		</ul>
		<ul class="nav navbar-nav navbar-right">
			<li><a href="<?php echo LR\Filters::escapeHtmlAttr($this->global->uiControl->link("Sign:Out")) ?>"> Odhlásit se</a></li>
		</ul>
<?php
		}
?>
	</div>
</nav>

	<div class="alert-wrap">
<?php
		$iterations = 0;
		foreach ($flashes as $flash) {
			?>	<div class="alert alert-<?php echo LR\Filters::escapeHtmlAttr($flash->type) /* line 46 */ ?> alert-dismissable">
		<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
		<?php echo LR\Filters::escapeHtmlText($flash->message) /* line 48 */ ?>

	</div><?php
			$iterations++;
		}
?>
</div>

<?php
		$this->renderBlock('content', $this->params, 'html');
?>

<?php
		$this->renderBlock('scripts', get_defined_vars());
?>
</body>
</html>
<?php
		return get_defined_vars();
	}


	function prepare()
	{
		extract($this->params);
		if (isset($this->params['flash'])) trigger_error('Variable $flash overwritten in foreach on line 46');
		Nette\Bridges\ApplicationLatte\UIRuntime::initialize($this, $this->parentName, $this->blocks);
		
	}


	function blockHead($_args)
	{
		
	}


	function blockScripts($_args)
	{
		extract($_args);
?>

	<script src="<?php echo LR\Filters::escapeHtmlAttr(LR\Filters::safeUrl($basePath)) /* line 55 */ ?>/js/bootstrap.min.js"></script>
	<script src="<?php echo LR\Filters::escapeHtmlAttr(LR\Filters::safeUrl($basePath)) /* line 56 */ ?>/js/netteForms.js"></script>
	<script src="<?php echo LR\Filters::escapeHtmlAttr(LR\Filters::safeUrl($basePath)) /* line 57 */ ?>/js/main.js"></script>

<?php
		if (isset($modalToggle)) {
?>
		<script>
            $(<?php echo LR\Filters::escapeJs($modalName) /* line 62 */ ?>).modal('hide');
		</script>
<?php
		}
?>

<?php
	}

}
