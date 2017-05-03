package sat_solver;

public class MakeCNF {

    private final char[] binaryOperators;
    private final char unaryOperator;
    private boolean alphabet;
    private boolean binary;
    private boolean unary;
    private char element;
    private int bracket;
    private String expression;
    private String updatedString;
    private boolean andExist;
    private boolean orExist;

    public MakeCNF() {
        this.unary = true;
        this.binary = false;
        this.alphabet = true;
        this.bracket = 0;
        this.binaryOperators = new char[]{'&', '|', '>', '='};
        this.unaryOperator = '~';
    }

    public MakeCNF(String expression) {
        this.unary = true;
        this.binary = false;
        this.alphabet = true;
        this.bracket = 0;
        this.binaryOperators = new char[]{'&', '|', '>', '='};
        this.unaryOperator = '~';
        this.expression = expression;
        updatedString = expression;
    }

    public boolean syntax() {

        for (int i = 0; i < this.expression.length(); i++) {
            element = this.expression.charAt(i);
            if (isAlphabet(element)) {
                if (this.alphabet == false) {
                    return false;
                }
                this.alphabet = false;
                this.binary = true;
                this.unary = false;
            } else if (isBinaryOperator(element)) {
                if (this.binary == false) {
                    return false;
                }
                this.alphabet = true;
                this.binary = false;
                this.unary = true;
            } else if ((element == this.unaryOperator)) {
                if (this.unary == false) {
                    return false;
                }
            } else if (element == '(') {
                if (this.binary == false) {
                    this.bracket += 1;
                } else {
                    return false;
                }
            } else if (element == ')') {
                if ((this.alphabet == false) && (this.bracket >= 0)) {
                    this.bracket -= 1;
                } else {
                    return false;
                }
            } else if (element == ' ') {
                return false;
            }
        }
        return ((this.bracket == 0) && (this.unary == false));
    }

    private boolean isAlphabet(char element) {
        return Character.isLetter(element);
    }

    private boolean isBinaryOperator(char element) {
        for (int i = 0; i < this.binaryOperators.length; i++) {
            if (this.binaryOperators[i] == element) {
                return true;
            }
        }
        return false;
    }

    public void Bijection() {
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '=') {
                handleBijection(i);
            }
        }
    }

    public void handleBijection(int index) {
        String leftSide = "";
        String midLeft = "";
        String rightSide = "";
        String midRight = "";

        for (int i = index - 1; i >= 0; i--) {
            if (expression.charAt(i) == ')') {
                this.bracket++;
            } else if (expression.charAt(i) == '(') {
                this.bracket--;
            }

            if (this.bracket == 0) {
                leftSide = expression.substring(0, i);
                midLeft = expression.substring(i, index);
                break;
            }

        }

        for (int i = index + 1; i < this.expression.length(); i++) {
            if (expression.charAt(i) == '(') {
                this.bracket++;
            } else if (expression.charAt(i) == ')') {
                this.bracket--;
            }

            if (this.bracket == 0) {
                rightSide = expression.substring(i + 1, expression.length());
                midRight = expression.substring(index + 1, i + 1);
                break;
            }
        }
        this.updatedString = leftSide + '(' + midLeft + ">" + midRight + ")&(" + midRight + ">" + midLeft + ')' + rightSide;
        this.expression = this.updatedString;
    }

    public void Implication() {
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '>') {
                handleImplication(i);
            }
        }
    }

    private void handleImplication(int index) {

        String leftSide = "";
        String midLeft = "";
        String rightSide = "";
        String midRight = "";

        for (int i = index - 1; i >= 0; i--) {
            if (expression.charAt(i) == ')') {
                this.bracket++;
            } else if (expression.charAt(i) == '(') {
                this.bracket--;
            }

            if (this.bracket == 0) {
                if ((i != 0) && (this.expression.charAt(i - 1) == '~')) {
                    leftSide = expression.substring(0, i - 1);
                    midLeft = expression.substring(i - 1, index);
                } else {
                    leftSide = expression.substring(0, i);
                    midLeft = expression.substring(i, index);
                }
                break;
            }
        }

        for (int i = index + 1; i < this.expression.length(); i++) {
            if (expression.charAt(i) == '(') {
                this.bracket++;
            } else if (expression.charAt(i) == ')') {
                this.bracket--;
            }

            if (this.bracket == 0) {
                rightSide = expression.substring(i + 1, expression.length());
                midRight = expression.substring(index + 1, i + 1);
                break;
            }
        }
        this.updatedString = leftSide + "(~" + midLeft + "|" + midRight + ")" + rightSide;
        this.expression = this.updatedString;

    }

    private void checkNegation() {
        char nextElement;
        boolean disable = false;
        int applyNegation = 0;
        String result = "";
        this.bracket = 0;

        for (int i = 0; i < this.expression.length(); i++) {
            this.element = this.expression.charAt(i);
//            System.out.println(result + " " + this.element);
            if (disable) {
                disable = false;
                continue;
            }

            if ((this.element != '~') && ((applyNegation % 2) == 0)) {
                if ((this.element == ')') && (applyNegation != 0) && (this.bracket == 0)) {
                    applyNegation--;
                }
                result += this.element;
            } else if ((this.element != '~') && ((applyNegation % 2) == 1)) {
                if (isAlphabet(this.element)) {
                    result += '~';
                    result += this.element;
                } else if (isBinaryOperator(this.element)) {
                    if (this.element == '&') {
                        result += '|';
                    } else {
                        result += '&';
                    }
                } else {
                    if ((this.element == ')') && (this.bracket != 0)) {
                        applyNegation--;
                    } else if (this.element == '(') {
                        this.bracket++;
                    } else if ((this.element == ')') && this.bracket != 0) {
                        this.bracket--;
                    }
                    result += this.element;
                }
            } else if (this.element == '~') {
                nextElement = this.expression.charAt(i + 1);
                if (nextElement == '~') {
                    disable = true;
                    continue;
                } else if (nextElement == '(') {
                    applyNegation++;
                    continue;
                } else if ((applyNegation % 2) == 1) {
                    result += nextElement;
                    disable = true;
                    continue;
                } else {
                    result += this.element;
                }
            }
        }

        this.expression = result;
    }

    private void removeBraces() {
        char e1, e2, e3;
        for (int i = 0; i < this.expression.length(); i++) {
            e1 = this.expression.charAt(i);

            if (e1 == '(') {
                e2 = this.expression.charAt(i + 1);
                if (e2 == '(') {
                    this.bracket = 0;
                    for (int j = i + 2; j < this.expression.length() - 1; j++) {
                        e3 = this.expression.charAt(j);
                        if ((this.bracket == 0) && (e3 == expression.charAt(j + 1) && (e3 == ')'))) {
                            String f = this.expression.substring(0, i);
                            String m = this.expression.substring(i + 1, j);
                            String t = this.expression.substring(j + 1, this.expression.length());
                            this.expression = f + m + t;
                        }

                        if (e3 == '(') {
                            this.bracket++;
                        }
                        if ((e3 == ')') && (this.bracket != 0)) {
                            this.bracket--;
                        }

                    }
                }
            }
        }

        for (int i = 0; i < this.expression.length() - 2; i++) {
            e1 = this.expression.charAt(i);
            e2 = this.expression.charAt(i + 2);
            if ((e1 == '(') && (e2 == ')')) {
                String f = this.expression.substring(0, i);
                String m = this.expression.substring(i + 1, i + 2);
                String t = this.expression.substring(i + 2, this.expression.length() - 1);
                this.expression = f + m + t;
            }
        }
        if((this.expression.charAt(0) == '(') && (this.expression.charAt(this.expression.length()-1) == ')'))
            this.expression = this.expression.substring(1, this.expression.length()-1);

    }

    private boolean verify() {
        char a;
        int bracket = 0;
        this.andExist = false;
        this.orExist = false;
        for (int i = 0; i < this.expression.length(); i++) {
            a = this.expression.charAt(i);

            if (a == '$') 
                this.andExist = true;

            else if(a == '|')
                this.orExist = true;
        }
        
        for (int i = 0; i < this.expression.length(); i++) {
            a = this.expression.charAt(i);
            
            if (a == '(') {
                bracket++;
            } 
            else if (a == ')') {
                bracket--;
            }

            if (a == '|') {
                if((!andExist) && (bracket < 0)) {
                    return false;
                } 
                else if((andExist) && (bracket <= 0)){
                    return false;
                }
            }

            if (a == '&') {
                if((!orExist) && (bracket < 0)) {
                    return false;
                } 
                else if((orExist) && (bracket != 0)){
                    return false;
                }
            }
        }
        
        for (int i = 0; i < this.expression.length(); i++) {
            a = this.expression.charAt(i);

            if (a == '(') {
                bracket++;
            } 
            else if (a == ')') {
                bracket--;
            }

        }
        return true;
    }

    public String makeCNF() {

        System.out.println("GIVEN EXPRESSION IS : " + this.expression);
        Bijection();
        Implication();
        checkNegation();
        removeBraces();

        System.out.println("AFTER CONVERSION TO CNF : " + this.expression);
        if (verify()) {
            return this.expression;
        } else {
            return "";
        }
    }
}
