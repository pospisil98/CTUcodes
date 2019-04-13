<?php

namespace App\Model;

use Nette;
use Nette\Security\Passwords;
use Tracy\Debugger;

/**
 * User management.
 */
class UserManager implements Nette\Security\IAuthenticator
{
	use Nette\SmartObject;

	const
		TABLE_NAME = 'users',
		COLUMN_ID = 'id',
		COLUMN_NAME = 'username',
		COLUMN_PASSWORD_HASH = 'hash';


	/** @var Nette\Database\Context */
	private $database;

    /**
     * UserManager constructor.
     *
     * @param Nette\Database\Context $database Database connection from DI
     */
	public function __construct(Nette\Database\Context $database) {
		$this->database = $database;
	}

    /**
    * Performs an authentication.
    *
    * @return Nette\Security\Identity
    * @throws Nette\Security\AuthenticationException
    */
	public function authenticate(array $credentials)
	{
		list($username, $password) = $credentials;

		$row = $this->database->table(self::TABLE_NAME)
			->where(self::COLUMN_NAME, $username)
			->fetch();

		if (!$row) {
			throw new Nette\Security\AuthenticationException('Uživatel neexistuje', self::IDENTITY_NOT_FOUND);

		} elseif (!Passwords::verify($password, $row[self::COLUMN_PASSWORD_HASH])) {
			throw new Nette\Security\AuthenticationException('Heslo není správné', self::INVALID_CREDENTIAL);

		} elseif (Passwords::needsRehash($row[self::COLUMN_PASSWORD_HASH])) {
			$row->update([
				self::COLUMN_PASSWORD_HASH => Passwords::hash($password),
			]);
		}

		$arr = $row->toArray();
		unset($arr[self::COLUMN_PASSWORD_HASH]);
		return new Nette\Security\Identity($row[self::COLUMN_ID], $arr);
	}

    /**
     * Performs an registration of new user.
     *
     * @param $username string  Given username to be added
     * @param $password string  Given password to be hashed and added
     */
	public function add($username, $password)
	{
		try {
			$this->database->table(self::TABLE_NAME)->insert([
				self::COLUMN_NAME => $username,
				self::COLUMN_PASSWORD_HASH => Passwords::hash($password),
			]);
		} catch (Nette\Database\UniqueConstraintViolationException $e) {
			throw new Nette\UnexpectedValueException("Uživatel již existuje");
		}
	}

    /**
     * Checks existence of an user in DB.
     *
     * @param $username
     * @return bool
     */
    public function checkExistance($username)
    {
        $row = $this->database->query("
            SELECT id
            FROM users
            WHERE username = ?
        ", $username)->fetch();

        //Debugger::barDump($row);
        if(!$row) {
            return false;
        } else {
            return true;
        }
    }
}
