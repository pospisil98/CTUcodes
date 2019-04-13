package cz.cvut.fel.pjv;

public class BruteForceAttacker extends Thief {          
    @Override
    public void breakPassword(int l) {
        // get chars from which we weill build password
        char[] charset = getCharacters();
        
        // start the bruteforce attack
        permutation(charset, "", charset.length, l);
    }

    private boolean permutation(char[] charset, String pswd, int len, int pos) {
        // if we have desired lenght try to opean vault
        if(pos == 0) {
            // if opened return true to stop other recursive calls otherwise continue
            if(tryOpen(pswd.toCharArray())) {
                return true;
            }
          
            return false;
        }

        // add new character
        for (int i = 0; i < len; i++) {
            // create new pswd with character added
            String newPswd = pswd + charset[i];
            
            // call this function on new password and check if we have to stop
            if(permutation(charset, newPswd, len, pos - 1)) {
                return true;
            }
        }
        return false;
    }
}