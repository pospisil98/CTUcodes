<?php

namespace App\Presenters;

use App\Model\UserManager;
use Nette;
use Nette\Application\UI\Form;
use Nette\Application\Responses\JsonResponse;
use Tracy\Debugger;

/**
 * Class SignPresenter
 * It is responsible for sign parts (in, up, out).
 *
 * @package App\Presenters
 */
class SignPresenter extends BasePresenter
{

    /** @var UserManager */
    private $userManager;

    /**
     * SignPresenter constructor.
     *
     * @param UserManager $userManager Model working with users table
     */
	public function __construct(UserManager $userManager)
	{
	    $this->userManager = $userManager;
	}

	/**
     * Default sign function - determines where to redirect user.
     */
	public function actionDefault() {
	    if(!$this->user->isLoggedIn()) {
	        $this->redirect("in");
        } else {
	        $this->redirect("Homepage:");
        }
    }

	public function actionIn() {
    }

    public function actionUp() {
    }

    /**
     * Function responsible for signing out the user.
     */
	public function actionOut()
	{
		$this->getUser()->logout();
		$this->redirect("Sign:in");
	}

    /**
     * Function creating the SignIn form.
     *
     * @return Form SignIn form with username and password
     */
	public function createComponentSignInForm() {
        $form = new Form();

        $form->addText('username')->setRequired();
        $form->addPassword('password')->setRequired();
        $form->addSubmit('submit', 'Přihlásit se');

        $form->onSuccess[] = array($this, 'signInFormSucceeded');

        return $form;
    }

    /**
     * Function creating the SignUp form.
     *
     * @return Form SignIn form with username and password
     */
    public function createComponentSignUpForm() {
        $form = new Form();

        $form->addText('username')->setRequired("Username je povinné")
            ->addRule(Form::MAX_LENGTH, "Username může mít maximálně %d znaků", 60);
        $form->addPassword('password')->setRequired("Heslo je povinné")
            ->addRule(Form::MAX_LENGTH, "Heslo může mít maximálně %d znaků", 60);
        $form->addPassword('passwordCheck')
            ->addRule(Form::EQUAL, 'Hesla se neshodují', $form['password'])
            ->setRequired("Hesla se musí shodovat");

        $form->addSubmit('submit', 'Zaregistrovat se');

        $form->onSuccess[] = array($this, 'signUpFormSucceeded');

        return $form;
    }

    /**
     * Function called after successful signIn form submit. It is responsible for loging in the user and
     * handling exceptions connected with it.
     *
     * @param $form Form SignIn form
     * @param $values array Array of values returned by SignIn form
     */
    public function signInFormSucceeded($form, $values) {
        $user = $this->getUser();

        try {
            $user->login($values->username, $values->password);
            $this->redirect('Homepage:');
        } catch (Nette\Security\AuthenticationException $e) {
            if($e->getCode() != 1) {
                $this->template->username = $values->username;
            }

            $this->flashMessage($e->getMessage(), 'danger');
        }

    }

    /**
     * Function called after successful signUp form submit. It is responsible for user registration and
     * handling registration exceptions.
     *
     * @param $form Form SignUp form
     * @param $values array Array of values returned by SignUp form
     */
    public function signUpFormSucceeded($form, $values) {
	    if(strlen($values['password']) > 60 or strlen($values['username']) > 60) {
            $this->flashMessage("Nesprávná délka username či hesla", 'danger');
            $this->template->username = $values->username;
        } else if($values['password'] != $values['passwordCheck']) {
            $this->flashMessage("Zadaná hesla se neshodují", 'danger');
            $this->template->username = $values->username;
        } else {
            try {
                $this->userManager->add($values['username'], $values['password']);
                $this->flashMessage("Zaregistrováno!", 'success');
                $this->redirect("in");
            } catch (Nette\UnexpectedValueException $e) {
                $this->flashMessage("Uživatelské jméno již existuje", 'danger');
                $this->template->username = $values->username;
            }
        }
    }

    /**
     * Function called by AJAX in signUp form to check existence
     * of given username.
     *
     * @param $username string Username to check
     */
    public function handleUsernameExists($username){
	    if($this->isAjax()) {
            $exists = $this->userManager->checkExistance($username);

            $this->sendResponse(new Nette\Application\Responses\JsonResponse(array('exists'=> $exists)));
        }
    }
}
