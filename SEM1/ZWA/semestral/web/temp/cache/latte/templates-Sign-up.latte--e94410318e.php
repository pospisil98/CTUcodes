<?php
// source: C:\Users\Vojcek\Documents\wamp64\www\semestral\app\presenters/templates/Sign/up.latte

use Latte\Runtime as LR;

class Templatee94410318e extends Latte\Runtime\Template
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
?>
<div class="register-wrapper text-center col-md-4 col-md-offset-4">
    <h1>NOTE.IT</h1>

<?php
		$form = $_form = $this->global->formsStack[] = $this->global->uiControl["signUpForm"];
		?>    <form id="signUpForm" method="post" role="form"<?php
		echo Nette\Bridges\FormsLatte\Runtime::renderFormBegin(end($this->global->formsStack), array (
		'id' => NULL,
		'method' => NULL,
		'role' => NULL,
		), false) ?>>
        <div id="formGroupUsername" class="form-group">
            <label for="username">Username</label>
            <input id="username" type="text" class="form-control" value="<?php
		if (isset($username)) {
			echo LR\Filters::escapeHtmlAttr($username) /* line 8 */;
		}
		?>" required maxlength="60"<?php
		$_input = end($this->global->formsStack)["username"];
		echo $_input->getControlPart()->addAttributes(array (
		'id' => NULL,
		'type' => NULL,
		'class' => NULL,
		'value' => NULL,
		'required' => NULL,
		'maxlength' => NULL,
		))->attributes() ?>>
        </div>
        <div class="form-group">
            <label for="password">Heslo</label>
            <input id="password" type="password" class="form-control" required maxlength="60"<?php
		$_input = end($this->global->formsStack)["password"];
		echo $_input->getControlPart()->addAttributes(array (
		'id' => NULL,
		'type' => NULL,
		'class' => NULL,
		'required' => NULL,
		'maxlength' => NULL,
		))->attributes() ?>>
        </div>
        <div class="form-group">
            <label for="passwordCheck">Heslo znovu</label>
            <input id="passwordCheck" type="password" class="form-control" required<?php
		$_input = end($this->global->formsStack)["passwordCheck"];
		echo $_input->getControlPart()->addAttributes(array (
		'id' => NULL,
		'type' => NULL,
		'class' => NULL,
		'required' => NULL,
		))->attributes() ?>>
        </div>
        <div class="form-group">
            <button class="btn center-block"<?php
		$_input = end($this->global->formsStack)["submit"];
		echo $_input->getControlPart()->addAttributes(array (
		'class' => NULL,
		))->attributes() ?>>Zaregistrovat!</button>
        </div>
<?php
		echo Nette\Bridges\FormsLatte\Runtime::renderFormEnd(array_pop($this->global->formsStack), false);
?>    </form>
    <div class="text-center">
        <a class="" href="<?php echo LR\Filters::escapeHtmlAttr($this->global->uiControl->link("Sign:in")) ?>">Přihlášení</a>
    </div>

    <script type="text/javascript">

        $('#username').on('change paste keyup', function() {
            var nameToTest = $("#username").val();
            //console.log(nameToTest);

            $.ajax({
                type: "GET",
                url: <?php echo LR\Filters::escapeJs($this->global->uiControl->link("usernameExists!")) ?>,
                data: { username : nameToTest},
                cache: false,
                success: function(payload){
                    console.log(payload);
                    var change = $("#formGroupUsername");
                    if(payload.exists) {
                        change.removeClass("has-success");
                        change.addClass("has-error");
                    } else {
                        change.removeClass("has-error");
                        change.addClass("has-success");
                    }
                }
            });
        });
    </script>
</div>
<?php
	}

}
