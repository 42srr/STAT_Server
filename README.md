## 2024.06.14

java version : 21.0.4
spring boot version : 3.2.5
---
#### Endpoint 관련 설명
`/projects` : `GET`
- 42 본과정 과제와 참여 중인 사람의 수를 반환합니다.

  
`/projects/intraid` : `GET`
- intraid에 해당하는 유저의 참여중인 과제를 반환합니다.

  
`/levels` : `GET`
- 현재 42경산 유저(lv 0.00 초과)의 레벨 분포를 정수 단위로 계산하여 각 레벨과 레벨에 해당하는 유저 수를 반환합니다.
```
{
    "0": 0,
    "1": 14,
    "2": 18,
    "3": 81,
    "4": 9,
    "5": 6,
    "6": 1,
    "7": 1,
    "8": 1,
    "9": 0,
    "10": 0,
    "11": 1,
    "12": 0,
    "13": 0,
    "14": 0,
    "15": 0,
    "16": 0,
    "17": 0,
    "18": 0,
    "19": 0,
    "20": 0
}
```


  
`/ranking/evalpoint` : `GET`
- 현재 42경산 유저(lv 0.00 초과)들의 평가 포인트 랭킹을 내림차순으로 {유저 프로필 사진, 유저 인트라 아이디, 평가포인트 개수}를 반환합니다.
```
[
    {
        "photo": "https://cdn.intra.42.fr/users/30eac113a3fc3aaf72524b0d1c7828bc/small_jemoon.jpg",
        "intraId": "jemoon",
        "evalPoint": 36
    },
    {
        "photo": "https://cdn.intra.42.fr/users/80bf2f21270e52de0b0a50a531376804/small_taejikim.jpg",
        "intraId": "taejikim",
        "evalPoint": 24
    },
    {
        "photo": "https://cdn.intra.42.fr/users/02a24aff0144f111c773598f02c52659/small_jihwkim2.jpg",
        "intraId": "jihwkim2",
        "evalPoint": 15
    }
]
```

`/ranking/wallet` : `GET`
- 현재 42경산 유저(lv 0.00 초과)들의 알타리안달러 랭킹을 내림차순으로 {유저 프로필 사진, 유저 인트라 아이디, 알타리안달러 개수}를 반환합니다.

- 
