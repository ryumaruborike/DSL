import java.util.*;
public class Inter {

    private ArrayList<Token> infixExpr;
    private Map<String, Double> variables = new HashMap<>();
    private Map<String, MyLinkedList> LLvariables = new HashMap<>();

    public int iterator;
    public Token cur;
    public boolean result_comparison_bool;
    public MyLinkedList list;
    public String LinkedList_var;

    private int operationPriority(Token op) {
        return switch (op.token) {
            case "(" -> 0;
            case "+", "-" -> 1;
            case "*", "/" -> 2;
            default -> -1;
        };
    }

    private double execute(Token op, double first, double second) {
        return switch (op.token) {
            case "+" -> first + second;
            case "-" -> first - second;
            case "*" -> first * second;
            case "/" -> first / second;
            default -> -1;
        };
    }

    public Inter(ArrayList<Token> infixExpr) {
        this.infixExpr = infixExpr;
        cur = infixExpr.get(0);
        iterator = 0;
        result_comparison_bool = false;
        run();
    }

    private void run() {
        for (; iterator < infixExpr.size(); iterator++) {
            cur = infixExpr.get(iterator);
            switch (cur.type) {
                case "ASSIGNMENT_OPERATOR" -> value_inter("ENDLINE");
                case "IF_OPERATION" -> if_inter();
                case "WHILE" -> interpret_while();
                case "DO" -> interpret_do_while();
                case "FOR" -> interpret_for();
                case "PRINT" -> interpret_print();
                case "LL" -> interpret_LL();
                case "POINT" -> interpret_LL_operator();
                case "PRNTLIST" -> interpret_LL_printlist();
            }
        }
    }

    private void interpret_LL_printlist() {
        cur = infixExpr.get(iterator);
            if (cur.type.equals("PRNTLIST")) {
                iterator++;
                cur = infixExpr.get(iterator);
                if (cur.type.equals("L_BC")){
                    iterator++;
                    cur = infixExpr.get(iterator);
                    if (cur.type.equals("VAR")) {
                        System.out.print("List " + cur.token + ": ");
                        LLvariables.get(cur.token).printLinkedList();
                        iterator++;
                    }
                }
            }
    }

    private void interpret_LL_operator() {
        int index_var = iterator - 1;
        LinkedList_var = infixExpr.get(index_var).token;
        int operator_index = iterator + 1;
        cur = infixExpr.get(operator_index);
        switch (cur.type){
            case "LLadd" -> interpret_LL_add();
            case "LLremove" -> interpret_LL_remove();
            case "LLget" -> interpret_LL_get();
            case "LLsize" -> interpret_LL_size();
        }
    }

    private void interpret_LL_size() {
        iterator++;
        cur = infixExpr.get(iterator);
        if (infixExpr.get(iterator + 1).type.equals("L_BC")){
            System.out.println("Size of " + LinkedList_var + " is " + LLvariables.get(LinkedList_var).size());
        }
    }

    private void interpret_LL_get() {
        iterator++;
        cur = infixExpr.get(iterator);
        if (cur.type.equals("LLget")){
            iterator+=2;
            cur = infixExpr.get(iterator);
            if (cur.type.equals("DIGIT")){
                System.out.println("element with index " + cur.token + " is " + LLvariables.get(LinkedList_var).get(Integer.parseInt(cur.token)));
            }
        }
        iterator++;
        cur = infixExpr.get(iterator);
    }

    private void interpret_LL_remove() {
        iterator++;
        cur = infixExpr.get(iterator);
        if (cur.type.equals("LLremove")){
            iterator+=2;
            cur = infixExpr.get(iterator);
            if (cur.type.equals("DIGIT")){
                LLvariables.get(LinkedList_var).remove(Integer.parseInt(cur.token));
            }
        }
        iterator++;
        cur = infixExpr.get(iterator);
    }

    private void interpret_LL_add() {
        iterator++;
        cur = infixExpr.get(iterator);
            if (cur.type.equals("LLadd")){
                iterator++;
                cur = infixExpr.get(iterator);
                if (cur.type.equals("L_BC")){
                    iterator++;
                    cur = infixExpr.get(iterator);
                    if (cur.type.equals("DIGIT")){
                        LLvariables.get(LinkedList_var).add(cur.token);
                        iterator++;
                        cur = infixExpr.get(iterator);
                        if (cur.type.equals("R_BC")){
                            iterator++;
                            cur = infixExpr.get(iterator);
                        }
                    }
                }
            }
        }

    private void interpret_LL() {
        cur = infixExpr.get(iterator);
        while (!cur.type.equals("ENDLINE")) {
            if ("LL".equals(cur.type)) {
                list = new MyLinkedList<>();
                System.out.println("LinkedList created");
            }
            if ("VAR".equals(cur.type)) {
                LLvariables.put(cur.token, list);
            }
            iterator++;
            cur = infixExpr.get(iterator);
        }
    }

    private void interpret_print() {
        iterator++;
        cur = infixExpr.get(iterator);
        if ("L_BC".equals(cur.type)) {
            Token c = infixExpr.get(iterator + 1);
            switch (c.type) {
                case "DIGIT" -> System.out.println(Double.parseDouble(c.token));
                case "VAR" -> System.out.println(c.token + " = " + variables.get(c.token));
            }
            iterator += 2;
        } else {
            System.out.println(variables);
        }
        iterator++;
    }



    private void interpret_do_while() {
        iterator += 2;
        cur = infixExpr.get(iterator);
        int start_iteration = iterator;

        do {
            value_inter("WHILE");
            condition_check();
            iterator = start_iteration;
            cur = infixExpr.get(iterator);
        } while (result_comparison_bool);
        while (!"ENDLINE".equals(cur.type)) {
            iterator++;
            cur = infixExpr.get(iterator);
        }
    }

    private void interpret_while() {
        int start_iteration = iterator;
        condition_check();
        while (result_comparison_bool) {
            iterator++;
            value_inter("ENDLINE");
            iterator = start_iteration;
            cur = infixExpr.get(iterator);
            condition_check();
        }

        while (!"ENDLINE".equals(cur.type)) {
            iterator++;
            cur = infixExpr.get(iterator);
        }
    }

    public void if_inter(){
        condition_check();
        if (!result_comparison_bool) {
            while (!"ENDLINE".equals(cur.type)) {
                if ("ELSE".equals(cur.type)) {
                    break;
                }
                iterator++;
                cur = infixExpr.get(iterator);
            }
        } else {
            iterator++;
            value_inter("ELSE");
            while (!"ENDLINE".equals(cur.type)) {
                iterator++;
                cur = infixExpr.get(iterator);
            }
        }
    }
    private void interpret_for() {
        iterator += 3;
        cur = infixExpr.get(iterator);
        value_inter("DIV");
        iterator--;
        int condition = iterator;
        condition_check();
        int indexAfterFor = iterator + 1;
        while (result_comparison_bool) {
            while (!"R_BC".equals(cur.type)) {
                iterator++;
                cur = infixExpr.get(iterator);
            }
            iterator += 2;
            cur = infixExpr.get(iterator);
            value_inter("ENDLINE");

            iterator = indexAfterFor;
            cur = infixExpr.get(iterator);
            value_inter("R_BC");

            iterator = condition;
            condition_check();
        }

        while (!"ENDLINE".equals(cur.type)) {
            iterator++;
            cur = infixExpr.get(iterator);
        }
    }
    public void value_inter(String trans){
        int indexVar = iterator - 1;
        int startExpr = iterator + 1;
        while (!trans.equals(cur.type)) {
            iterator++;
            cur = infixExpr.get(iterator);
        }

        double rez = calc(toPostfix(infixExpr, startExpr, iterator));
        variables.put(infixExpr.get(indexVar).token, rez);
    }
    public void condition_check(){
        int first_argument_index = iterator + 2;
        int second_argument_index = iterator + 4;
        int comparison_op_index = iterator + 3;
        Token s = infixExpr.get(second_argument_index);
        double first_argument = variables.get(infixExpr.get(first_argument_index).token);
        double second_argument = switch (s.type){
            case "DIGIT" -> Double.parseDouble(s.token);
            case "VAR" -> variables.get(s.token);
            default -> 0.0;
        };
        iterator = iterator + 6;
        cur = infixExpr.get(iterator);
        result_comparison_bool = compare(infixExpr.get(comparison_op_index), first_argument, second_argument);
    }

    private boolean compare(Token comp, double first_argument, double second_argument) {
        return switch (comp.token){
            case ">" -> Double.compare(first_argument, second_argument) == 1;
            case "<" -> Double.compare(first_argument, second_argument) == -1;
            case "~" -> Double.compare(first_argument, second_argument) == 0;
            default -> throw new IllegalArgumentException("Illegal value: " + comp.token);
        };
    }
    private ArrayList<Token> toPostfix(ArrayList<Token> infixExpr, int start, int end) {
        //	Выходная строка, содержащая постфиксную запись
        ArrayList<Token> postfixExpr = new ArrayList<>();
        //	Инициализация стека, содержащий операторы в виде символов
        Stack<Token> stack = new Stack<>();

        //	Перебираем строку
        for (int i = start; i < end; i++) {
            //	Текущий символ
            Token c = infixExpr.get(i);
            //	Если симовол - цифра
            if (c.type.equals("DIGIT") || c.type.equals("VAR")) {
                postfixExpr.add(c);
            } else if (c.type.equals("L_BC")) { //	Если открывающаяся скобка
                //	Заносим её в стек
                stack.push(c);
            } else if (c.type.equals("R_BC")) {//	Если закрывающая скобка
                //	Заносим в выходную строку из стека всё вплоть до открывающей скобки
                while (stack.size() > 0 && stack.peek().type != "L_BC")
                    postfixExpr.add(stack.pop());
                //	Удаляем открывающуюся скобку из стека
                stack.pop();
            } else if (c.type == "OPERATOR") { //	Проверяем, содержится ли символ в списке операторов
                Token op = c;
                //	Заносим в выходную строку все операторы из стека, имеющие более высокий приоритет
                while (stack.size() > 0 && (operationPriority(stack.peek()) >= operationPriority(op)))
                    postfixExpr.add(stack.pop());
                //	Заносим в стек оператор
                stack.push(c);
            }
        }
        //	Заносим все оставшиеся операторы из стека в выходную строку
        postfixExpr.addAll(stack);

        //	Возвращаем выражение в постфиксной записи
        return postfixExpr;
    }

    private double calc(ArrayList<Token> postfixExpr) {
        //	Стек для хранения чисел
        Stack<Double> locals = new Stack<>();
        //	Счётчик действий
        int counter = 0;

        //	Проходим по строке
        for (int i = 0; i < postfixExpr.size(); i++) {
            //	Текущий символ
            Token c = postfixExpr.get(i);

            //	Если символ число
            if (c.type.equals("DIGIT")) {
                String number = c.token;
                locals.push(Double.parseDouble(number));
            } else if (c.type.equals("VAR")) {
                locals.push(variables.get(c.token));
            } else if (c.type.equals("OPERATOR")) { //	Если символ есть в списке операторов
                //	Прибавляем значение счётчику
                counter++;

                //	Получаем значения из стека в обратном порядке
                double second = locals.size() > 0 ? locals.pop() : 0,
                        first = locals.size() > 0 ? locals.pop() : 0;

                //	Получаем результат операции и заносим в стек
                locals.push(execute(c, first, second));
            }
        }

        //	По завершению цикла возвращаем результат из стека
        return locals.pop();
    }

    public Map<String, Double> getVariables() {
        return variables;
    }
    public Map<String, MyLinkedList> getLLvariables() {
        return LLvariables;
    }
}