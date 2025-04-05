package ggs.srr.service.user;

import ggs.srr.exception.service.user.NotFoundRankingTypeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class RankingTypeTest {

    @DisplayName("랭킹 타입에 존재하는 타입으로 랭킹 타입 enum 조회시 일치하는 타입 객체가 반환된다.")
    @ParameterizedTest
    @ValueSource(strings = {"level", "wallet", "collectionPoint"})
    void of(String type) {
        //given //when
        RankingType rankingType = RankingType.of(type);

        //then
        assertThat(rankingType.getValue()).isEqualTo(type);
    }

    @DisplayName("없는 타입으로 조회시 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"없음", "진짜 없음", "레베루"})
    void ofNotExistType(String type) {
        //when then

        assertThatThrownBy(() -> RankingType.of(type))
                .isInstanceOf(NotFoundRankingTypeException.class)
                .hasMessage("해당하는 랭킹 기준을 찾을 수 없습니다.");
    }

}