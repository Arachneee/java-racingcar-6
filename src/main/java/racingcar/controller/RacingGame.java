package racingcar.controller;

import static racingcar.constant.RacingGameConstant.CARS_SPLIT_STRING;
import static racingcar.constant.RacingGameConstant.REGEX;
import static racingcar.constant.RacingGameConstant.ZERO;
import static racingcar.exception.ErrorMessage.NOT_INTEGER;

import camp.nextstep.edu.missionutils.Console;
import java.util.Arrays;
import java.util.List;
import racingcar.domain.Cars;
import racingcar.dto.CarsDto;
import racingcar.exception.RacingGameException;
import racingcar.view.InputView;
import racingcar.view.OutputView;

public class RacingGame {

    private static final InputView inputView = new InputView();
    private static final OutputView outputView = new OutputView();
    private Integer rotateCount;
    private Cars cars;

    public RacingGame() {
    }

    public void run() {
        init();

        rotate();

        finish();
    }

    private void init() {
        generateCars();
        askRotateNumber();
    }

    private void generateCars() {
        final String carNames = inputView.enterCarNames();

        cars = Cars.from(splitString(carNames));
    }

    private void askRotateNumber() {
        final String rotateNumber = inputView.enterRotateNumber();

        rotateCount = convertToInteger(rotateNumber);
    }

    private List<String> splitString(String carNames) {
        return Arrays.stream(carNames.split(CARS_SPLIT_STRING))
                .toList();
    }

    private Integer convertToInteger(String inputNumberString) {
        validateNumber(inputNumberString);

        return Integer.valueOf(inputNumberString);
    }

    private void validateNumber(String inputNumberString) {
        if (isNotNumber(inputNumberString)) {
            throw RacingGameException.of(NOT_INTEGER);
        }

    }

    private boolean isNotNumber(String inputNumberString) {
        return !inputNumberString.matches(REGEX);
    }

    private void rotate() {
        outputView.printRunResult();

        while (isRemainRotateCount()) {
            raceStart();
            subRotateCount();
        }
    }

    private void subRotateCount() {
        rotateCount--;
    }

    private boolean isRemainRotateCount() {
        return rotateCount > ZERO;
    }

    private void raceStart() {
        cars.go();

        outputView.printCarsResults(CarsDto.from(cars));
    }

    private void finish() {
        outputView.printWinners(cars.findWinnerNameList());

        Console.close();
    }
}
