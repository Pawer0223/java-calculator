package calculator.serviceImpl;

import calculator.engine.calculate.CalculateImpl;
import calculator.engine.sorter.BasicSorter;
import calculator.engine.model.OperatorOrder;
import calculator.engine.utils.Util;
import calculator.engine.calculate.Calculate;
import calculator.engine.sorter.Sorter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.*;

public class CalculationImplTest {

    private Calculate calculation = new CalculateImpl();
    private Sorter sorter = new BasicSorter();

    private void initCalculateData(String[] splitArr, Double[] nums, List<OperatorOrder> orderedOperators) {
        for (int i = 0; i < splitArr.length; i++) {
            if (i % 2 == 0)
                nums[i] = Double.valueOf(splitArr[i]);
            else {
                orderedOperators.add(new OperatorOrder(splitArr[i].charAt(0), i));
            }
        }
        sorter.sort(orderedOperators);
    }

    private void testFunction(String command, double expected) {
        // when
        String[] splitArr = command.split(" ");
        Double[] nums = new Double[splitArr.length];
        List<OperatorOrder> orderedOperators = new ArrayList<>();
        initCalculateData(splitArr, nums, orderedOperators);
        double actual = calculation.calculate(nums, orderedOperators);
        System.out.printf("command : [%s], expected: [%s], actual : [%f]\n", command, expected, actual);
        // then
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource({ // given
            "1 + 2, 3",
            "1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1, 10",
            "1 + 2 + 3 + 4 + 5, 15"
    })
    public void 계산_덧셈_테스트(String command, double expected) {
        testFunction(command, expected);
    }

    @ParameterizedTest
    @CsvSource({ // given
            "0 - 1 - 1 - 1 - 1 - 1, -5",
            "10 - 11, -1",
            "-1 - 5, -6"
    })
    public void 계산_뺄셈_테스트(String command, double expected) {
        testFunction(command, expected);
    }

    @ParameterizedTest
    @CsvSource({ // given
            "-2 * 2 * 1, -4",
            "2 * 2 * 1, 4",
            "-2 * 2 * 1, -4",
            "2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2, 1024"
    })
    public void 계산_곱셈_테스트(String command, double expected) {
        testFunction(command, expected);
    }

    @ParameterizedTest
    @CsvSource({ // given
            "20 / 2 / 2, 5",
            "20 / 2 / 2 / 2, 2.5",
            "20 / 2 / 2 / 2 / 2, 1.25",
            "20 / 2 / 2 / 2 / 2 / 2, 0.625",
    })
    public void 계산_나눗셈_테스트(String command, double expected) {
        testFunction(command, expected);
    }

    @ParameterizedTest
    @CsvSource({ // given
            "20 / 0",
            "2 * 3 * 1 / 0",
    })
    public void 계산_나눗셈_0으로나누는_경우_테스트(String command) {
        // when
        System.out.printf("command : [%s]\n", command);
        String[] splitArr = command.split(" ");
        Double[] nums = new Double[splitArr.length];
        List<OperatorOrder> orderedOperators = new ArrayList<>();
        initCalculateData(splitArr, nums, orderedOperators);
        Assertions.assertThrows(RuntimeException.class, () -> calculation.calculate(nums, orderedOperators));
    }

    @ParameterizedTest
    @CsvSource({ // given
           "1 + 2 * 3 / 4 + 1, 3.5",
            "1 / 2 * 3 - 1 - 0 / 2, 0.5",
            "1 * 0 + 1 - 1 / 10, 0.9"
    })
    public void 계산_혼합_테스트(String command, double expected) {
        testFunction(command, expected);
    }

    @Test
    public void 뒤에_곱하기_나누기가나오는_3개연산자_테스트() {
        Map<Integer, Character> ops = new HashMap<>();
        ops.put(0, '+');
        ops.put(1, '-');
        // 우선순위가 높은게 뒤에나오도록 테스트 함
        ops.put(2, '/');
        ops.put(3, '*');

        Random r = new Random();
        int testCase = 1000;

        for (int i = 0; i < testCase; i++) {
            double num1 = getNum(r);
            double num2 = getNum(r);
            double num3 = getNum(r);

            // + or -
            char op1 = ops.get(getNextOpIdx(r, 0));
            // all
            char op2 = ops.get(getNextOpIdx(r, 2));

            double temp = Util.callExec(op2, num2, num3);
            double expected = Util.callExec(op1, num1, temp);

            StringBuilder sb = new StringBuilder();
            sb.append(num1 + " " + op1 + " " + num2 + " " + op2 + " " + num3);
            testFunction(sb.toString(), expected);
        }
    }

    private int getNextOpIdx(Random r, int i) {
        return r.nextInt(2) + i;
    }

    private int getNum(Random r) {
        return r.nextInt(100) + 1;
    }
}

