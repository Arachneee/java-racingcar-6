package racingcar.view;

import static org.assertj.core.api.Assertions.assertThat;

import camp.nextstep.edu.missionutils.Console;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class InputViewTest {

    InputView inputView = new InputView();
    OutputStream out = new ByteArrayOutputStream();

    @BeforeEach
    void open() {
        System.setOut(new PrintStream(out));
    }

    @AfterEach
    void close() {
        Console.close();
    }

    @Test
    @DisplayName("자동차 이름 입력받기")
    void 차이름_입력() {
        // given
        systemIn("aa,bb");

        // when
        String names = inputView.enterCarNames();

        // then
        assertThat(out.toString()).contains("경주할 자동차 이름을 입력하세요.(이름은 쉼표(,) 기준으로 구분)");
        assertThat(names).isEqualTo("aa,bb");
    }

    @Test
    @DisplayName("시도 횟수 입력받기")
    void 시도_횟수_입력() {
        // given
        systemIn("5");

        // when
        String number = inputView.enterRotateNumber();

        // then
        assertThat(out.toString()).contains("시도할 회수는 몇회인가요?");
        assertThat(number).isEqualTo("5");
    }

    public static void systemIn(String input) {
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
    }
}
