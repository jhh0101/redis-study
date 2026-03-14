# WebSocket Front-end Guide
## React
### 1. 프론트엔드 연동 서비스(Service) 작성 가이드
```javascript
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';

class WebSocketService {
    constructor() {
        this.stompClient = null;
    }

    // 1. 백엔드와 웹소켓 연결
    connect(onConnected) {
        // 백엔드 WebSocketConfig에서 설정했던 엔드포인트
        const socket = new SockJS('http://localhost:8080/ws-auction'); // .env 파일로 관리
        this.stompClient = Stomp.over(socket);
        
        // 콘솔에 stomp 로그 찍히는거 끄기 (선택사항)
        this.stompClient.debug = null; 

        this.stompClient.connect({}, () => {
            console.log("웹소켓 연결 성공!");
            if (onConnected) onConnected();
        });
    }

    // 2. 특정 상품 채널 구독 및 메시지 수신 대기
    subscribeToItem(itemId, callback) {
        if (this.stompClient) {
            // 백엔드가 RTopic으로 쏴주는 채널 주소
            return this.stompClient.subscribe(`/topic/item/${itemId}`, (message) => {
                // 메시지가 도착하면 콜백 함수 실행 (새로운 가격 전달)
                callback(message.body); 
            });
        }
    }

    // 3. 연결 끊기 (페이지 나갈 때 필수)
    disconnect() {
        if (this.stompClient !== null) {
            this.stompClient.disconnect();
            console.log("웹소켓 연결 해제");
        }
    }
}

// 싱글톤으로 하나만 만들어서 내보냄
export default new WebSocketService();
```

## 2. 프론트엔드 연동 훅(Hook) 작성 가이드

웹소켓을 화면에 연결할 때는 컴포넌트 생명주기(Lifecycle) 관리를 위해 반드시 Custom Hook으로 분리해서 사용합니다. 프로젝트 환경에 맞춰 아래 두 가지 방법 중 하나를 선택하세요.

### 방법 A: 기본 구현 (useState 사용)
가장 단순한 형태입니다. React Query 없이 단순 상태 관리만 필요할 때 사용합니다.

```javascript
import { useState, useEffect } from 'react';
import websocketService from '../services/websocketService';

export const useAuctionWebSocketBasic = (itemId, initialPrice) => {
    const [currentPrice, setCurrentPrice] = useState(initialPrice);

    useEffect(() => {
        // 화면 진입 시 웹소켓 연결 및 구독
        websocketService.connect(() => {
            websocketService.subscribeToItem(itemId, (newPrice) => {
                setCurrentPrice(Number(newPrice)); 
            });
        });

        // 화면 이탈 시 웹소켓 연결 해제
        return () => websocketService.disconnect();
    }, [itemId]);

    return currentPrice;
};
```
---
### 방법 B: 실무 권장 구현 (React Query 연동) 🌟
Axios로 초기 데이터를 가져오고, 실시간 변동분만 웹소켓으로 받아와서 React Query의 캐시를 직접 업데이트하는 방식입니다. (API 호출과 실시간 동기화가 결합된 구조)
```javascript
import { useEffect } from 'react';
import { useQueryClient } from '@tanstack/react-query';
import websocketService from '../services/websocketService';

// 주의: 이 훅은 컴포넌트에서 useQuery로 데이터를 먼저 불러온 뒤에 호출해야 합니다.
export const useAuctionWebSocketWithQuery = (itemId) => {
    const queryClient = useQueryClient();

    useEffect(() => {
        websocketService.connect(() => {
            websocketService.subscribeToItem(itemId, (newPrice) => {
                
                // 웹소켓 메시지 수신 시, React Query 캐시를 강제 업데이트!
                // 백엔드에 API 재요청(refetch)을 하지 않고도 화면이 즉시 최신화됩니다.
                queryClient.setQueryData(['auctionItem', itemId], (oldData) => {
                    // oldData가 null이면 undefined 리턴(로딩 화면 출력됨)
                    if (!oldData) return undefined;
                    // ...oldData << 기존의 데이터는 건들지 않음 
                    // currentPrice: Number(newPrice) << 웹 소켓으로 받은 변경된 입찰가만 데이터를 수정해줌
                    return { ...oldData, currentPrice: Number(newPrice) }; 
                });
                
            });
        });

        return () => websocketService.disconnect();
    }, [itemId, queryClient]);
};
```
### 3. View 사용 예시 (Usage)
프론트엔드 상세 페이지에서 아래와 같이 한 줄로 연동을 마무리합니다.

```javascript
// 초기 데이터 조회 + 실시간 가격 업데이트가 통합된 훅
const { data: item } = useAuctionItem(itemId);

// UI는 데이터의 출처를 신경 쓰지 않고 item.currentPrice만 렌더링
return <span>{item.currentPrice}원</span>;
```