## 2024.06.14

java version : 21.0.4
spring boot version : 3.2.5
---
#### Endpoint 관련 설명
`/projects` : `GET`
- 42 본과정 과제와 참여 중인 사람의 수를 반환합니다.
```
{
    "netwhat": 0,
    "minitalk": 5,
    "AlCu": 0,
    "ft_linux": 0,
    "kfs-2": 0,
    "internship I - Contract Upload": 0,
    "Libft": 5,
    "webserv": 0,
    "libasm": 0,
    "Internship I": 1,
    "ft_containers": 0,
    "[DEPRECATED] Python Module 00": 1,
    "[DEPRECATED] Piscine Python Django": 1,
    "CPP Module 09": 1,
    "miniRT": 0,
    "minishell": 34,
    "fract-ol": 3,
    "FdF": 4,
    "get_next_line": 13,
    "ft_services": 0,
    "Day 03": 1,
    "Day 02": 1,
    "Day 01": 1,
    "Day 00": 1,
    "Rushes": 1,
    "push_swap": 15,
    "ft_printf": 17,
    "Day 09": 1,
    "so_long": 5,
    "Day 08": 1,
    "Hotrace": 0,
    "Day 07": 1,
    "Day 06": 1,
    "internship I - Company Mid Evaluation": 0,
    "Day 05": 1,
    "Day 04": 1,
    "Exam Rank 03": 44,
    "Philosophers": 59,
    "Exam Rank 04": 8,
    "Rush 00": 0,
    "Exam Rank 02": 99,
    "Rush 01": 0,
    "internship I - Company Final Evaluation": 0,
    "ft_transcendence": 0,
    "Born2beroot": 17,
    "Exam Rank 05": 4,
    "Exam Rank 06": 0,
    "Wong kar Wai": 0,
    "pipex": 6,
    "CPP Module 00": 1,
    "CPP Module 03": 0,
    "CPP Module 04": 0,
    "yasl": 0,
    "CPP Module 01": 0,
    "CPP Module 02": 1,
    "CPP Module 07": 0,
    "CPP Module 08": 0,
    "CPP Module 05": 2,
    "CPP Module 06": 0,
    "AutomaticDirectory": 0,
    "NetPractice": 1,
    "cub3d": 0,
    "ft_server": 0,
    "Internship I - Duration": 0,
    "boot2root": 0,
    "ladder": 1,
    "ft_irc": 0,
    "ActiveDiscovery": 1,
    "Inception": 5
}
```

  
`/projects/intraid` : `GET`
- intraid에 해당하는 유저의 참여중인 과제를 서버 Log에 남깁니다.


  
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
```
[
    {
        "photo": "https://cdn.intra.42.fr/users/bcd026b5a1be87890c9893dbc6c5db8a/small_taeng.gif",
        "intraId": "taeng",
        "dollar": 3813
    },
    {
        "photo": "https://cdn.intra.42.fr/users/fcea0110d5a42e08e3e763eb5d78695f/small_bonikoo.jpg",
        "intraId": "bonikoo",
        "dollar": 137
    }
]
```

- 
