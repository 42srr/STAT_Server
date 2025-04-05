package ggs.srr.service.user;

import ggs.srr.exception.service.user.NotFoundRankingTypeException;

import java.util.Arrays;

public enum RankingType {
    LEVEL("level"), WALLET("wallet"), COLLECTION_POINT("collectionPoint");

    private final String value;

    RankingType(String value) {
        this.value = value;
    }

    public static RankingType of(String value) {
        return Arrays.stream(values())
                .filter(type -> type.value.equals(value))
                .findAny()
                .orElseThrow(() -> new NotFoundRankingTypeException("해당하는 랭킹 기준을 찾을 수 없습니다."));
    }

    public String getValue() {
        return value;
    }
}
