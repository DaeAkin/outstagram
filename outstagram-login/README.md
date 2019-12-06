# login

gradle 멀티모듈에서는 필요한 의존성을 전부 주입하지말고, 각 모듈마다 지정해주는게 좋음.
configserver 인식이 안되서 애먹었음. (login 모듈이 config-server 라이브러리를 갖고있어서 오류)

