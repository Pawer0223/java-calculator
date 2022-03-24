package service;

import static java.lang.Character.isWhitespace;
import static utils.ValidationUtil.isNumberWithWhite;
import static utils.ValidationUtil.isOperator;

public class CommandFilterImpl implements CommandFilter {

    @Override
    public String filter(String command) {
        int i = 0;
        int len = command.length();
        StringBuilder sb = new StringBuilder();

        while (i < len && isWhitespace(command.charAt(i)))
            i++;
        if (command.charAt(i) == '-') {
            sb.append('-');
            i++;
        }
        while (i < len) {
            while (i < len && isNumberWithWhite(command.charAt(i))) {
                if (!isWhitespace(command.charAt(i)))
                    sb.append(command.charAt(i));
                i++;
            }
            if (i == len)
                break;
            if (i < len && isOperator(command.charAt(i))) {
                sb.append(' ').append(command.charAt(i)).append(' ');
            }
            i++;
        }
        return sb.toString();
    }
}
