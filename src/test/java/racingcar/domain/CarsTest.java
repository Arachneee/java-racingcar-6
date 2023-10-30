package racingcar.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CarsTest {

    @Test
    @DisplayName("자동차들 정상 생성")
    void Cars_정상_생성() {
        // given
        List<String> nameList = List.of("a", "bb", "ccc", "dddd");

        // when
        Cars cars = Cars.of(nameList, new RandomNumberGenerator());

        // then
        assertThat(cars.getCars().size()).isEqualTo(4);
    }

    @Test
    @DisplayName("자동차들 이름에 5글자 초과가 들어올 수 없다.")
    void Cars_비정상_생성() {
        // given
        List<String> nameList = List.of("a", "bb", "ccc", "dddddd");

        // when then
        assertThatThrownBy(() -> Cars.of(nameList, new RandomNumberGenerator()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("자동차들의 이름이 공백일 수 없다.")
    void Cars_비정상_생성_이름없음() {
        // given
        List<String> nameZero = List.of("", " ");

        // when then
        assertThatThrownBy(() -> Cars.of(nameZero, new RandomNumberGenerator()))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    @DisplayName("최대 위치 추출")
    void 최대_위치_추출() {
        // given
        List<String> nameList = List.of("a", "b", "c", "d", "e");
        Cars cars = Cars.of(nameList, new RandomNumberGenerator());
        int n = 10;

        // when
        while (n-- > 0) {
            cars.goByNumber();
        }

        // then
        Integer maxValue = cars.getCars()
                .stream()
                .max(Comparator.comparing(Car::getPosition)).get().getPosition();

        List<String> winnerNameList = cars.findWinnerNameList();

        List<Integer> list = cars.getCars()
                .stream()
                .filter(car -> winnerNameList.contains(car.getName()))
                .map(Car::getPosition)
                .distinct()
                .toList();

        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0)).isEqualTo(maxValue);
    }

    @Test
    @DisplayName("우승자 추출")
    void 우승자_이름_추출() {
        // given
        List<String> nameList = List.of("a", "b", "c", "d", "e");
        Cars cars = Cars.of(nameList, new RandomNumberGenerator());
        int n = 10;

        // when
        while (n-- > 0) {
            cars.goByNumber();
        }

        // then
        Car maxCar = cars.getCars()
                .stream()
                .max(Comparator.comparing(Car::getPosition)).get();

        assertThat(cars.findWinnerNameList())
                .contains(maxCar.getName());
    }

    @Test
    @DisplayName("공동 우승자도 추출된다.")
    void 공동_우승자_추출() {
        // given
        Cars cars = Cars.of(List.of("a", "b", "c", "d", "e"), new RandomNumberGenerator());
        int n = 10;

        // when
        while (n-- > 0) {
            cars.goByNumber();
        }

        // then
        Integer maxPosition = cars.getCars()
                .stream()
                .mapToInt(Car::getPosition)
                .max().getAsInt();

        List<String> carList = cars.getCars()
                .stream().filter(car -> car.getPosition().equals(maxPosition))
                .map(Car::getName)
                .toList();

        assertThat(cars.findWinnerNameList())
                .isEqualTo(carList);
    }

    @Test
    @DisplayName("자동차들 객체를 외부 변경으로부터 보호해야한다.")
    void 외부_변경_빈리스트_보호() {
        // given
        Cars beforeCars = Cars.of(List.of("a", "b", "c"), new RandomNumberGenerator());

        // when
        List<Car> carList = beforeCars.getCars();

        carList = new ArrayList<>();

        // then
        assertThat(beforeCars.getCars().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("자동차들 객체를 외부에서 객체 추가 보호해야한다.")
    void 외부_추가_보호() {
        // given
        Cars beforeCars = Cars.of(List.of("a", "b", "c"), new RandomNumberGenerator());

        // when
        List<Car> carList = beforeCars.getCars();

        // then
        assertThatThrownBy(() -> carList.add(Car.from("d")))
                .isInstanceOf(UnsupportedOperationException.class);

    }

    @Test
    @DisplayName("자동차들 객체 내부의 자동차 객체도 보호해야한다.")
    void 외부_변경_리스트_내부_변경_보호() {
        // given
        Cars beforeCars = Cars.of(List.of("a", "b", "c"), new RandomNumberGenerator());

        // when
        List<Car> carList = beforeCars.getCars();
        Car car = carList.get(0);
        car = Car.from("d");

        // then
        assertThat(beforeCars.getCars().get(0).getName()).isEqualTo("a");
    }
}