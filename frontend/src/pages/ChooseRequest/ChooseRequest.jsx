import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

// icons
import icon_exchange from 'assets/icons/exchange.svg';

// Component
import FormSubmitBtn from 'components/common/FormSubmitBtn';
import TopBar2 from 'components/common/TopBar2';
import ProductListItem from 'components/common/ProductListItem';

// twin.macro
import tw, { styled, css } from 'twin.macro';

// api
import { get_user_item } from 'api/apis/user';
// store
import { makeRequestStore } from '../../store/makeRequest';
import { useQuery } from 'react-query';
import { get_item } from 'api/apis/item';

// Styled Component
const Wrapper = styled.div``;

const TextContainer = styled.div`
  ${tw`p-2`}
  h3 {
    ${tw`text-h3 text-black`}
  }
  span {
    ${tw`text-tiny text-grey2`}
  }
`;

const ItemList = styled.div`
  ${tw`overflow-scroll overflow-x-hidden`}
  ${css`
    height: calc(100vh - 430px);
  `}
`;

const HeadingBar = tw.div`w-full h-[60px] p-2 text-h3 border-b bg-white`;

const SelectedItemContainer = styled.div`
  ${tw`flex m-2 h-[100px] items-center gap-2`}
  ${props => (props.cnt ? tw`` : tw`justify-center`)}
  span {
    ${tw`text-sub text-grey3`}
  }
`;

const BagguPreviewContainer = styled.div`
  ${tw`flex m-2 h-[100px] items-center gap-2`}
  ${props => (props.cnt ? tw`` : tw`justify-center`)}
  span {
    ${tw`text-sub text-grey3`}
  }
`;

const SelectedItem = styled.div`
  ${tw`rounded border`}
  ${tw`w-[100px] h-[100px] bg-cover bg-center`}
  ${props =>
    css`
      background-image: url(${props.item.itemImgUrl});
    `}
`;

// Main Component
function ChooseRequest() {
  // 바꾸 신청 대상 아이템 pk
  const { itemIdx } = useParams();

  // 작성자의 userIdx
  const userIdx = localStorage.getItem('userIdx');

  // 이 게시글 정보
  const [item, setItem] = useState([]);

  // 바꾸 신청자가 선택한 아이템의 itemIdx
  const [selectedItems, setSelectedItems] = useState();

  const { data } = useQuery(
    ['getItem', { itemIdx: itemIdx }],
    async () => await get_item(itemIdx),
    { onSuccess: data => setItem(data) }
  );

  useEffect(() => {}, []);

  // userItem에서 itemIdx로 item 객체를 찾아 반환하는 함수
  const findItem = itemIdx => {
    // return userItems.find(item => item.itemIdx === itemIdx);
  };
  // 제출 버튼 비활성화 여부
  // const disabled = selectedItems ? false : true;

  // 제출 버튼 클릭시 작동하는 함수
  const navigate = useNavigate();
  const onClickHandler = () => {
    // if (disabled) return;
    // store에 저장

    navigate(`/makeRequest/message/${itemIdx}`);
  };
  // 바꾸신청 API

  return (
    <div>
      <TopBar2 title="" />
      <TextContainer>
        <h3>교환하고 싶은 물건을 선택해주세요.</h3>
        <span>1개만 선택 가능합니다.</span>
      </TextContainer>
      <BagguPreviewContainer>
        <div></div>
        <img src={icon_exchange} alt="" />
        <div></div>
      </BagguPreviewContainer>
      {/* <SelectedItemContainer>
        {selectedItems.length ? (
          selectedItems.map(itemIdx => (
            <SelectedItem
              id="selectedItem"
              key={`selected ${itemIdx}`}
              item={findItem(itemIdx)}
            />
          ))
        ) : (
          <span>선택된 아이템이 없습니다.</span>
        )}
      </SelectedItemContainer> */}
      <HeadingBar>
        <h3>내 아이템</h3>
      </HeadingBar>
      {/* <ItemList>
        {userItems ? (
          // userItems.map(item => (
          <ProductListItem
            key={item.itemIdx}
            id={item.itemIdx}
            item={item}
            onClick={e => {
              setSelectedItems(prev => {
                // 선택한 아이템이 3개 이상이 되면
                if (prev.length >= 3) {
                  if (prev.includes(item.itemIdx)) {
                    return prev.filter(idx => idx !== item.itemIdx);
                  } else {
                    return prev;
                  }
                } else {
                  if (prev.includes(item.itemIdx)) {
                    return prev.filter(idx => idx !== item.itemIdx);
                  } else {
                    return [...prev, item.itemIdx];
                  }
                }
              });
            }}
            selected={selectedItems.includes(item.itemIdx)}
          />
        ) : (
          <span>등록된 아이템이 없습니다.</span>
        )}
      </ItemList> */}
      <FormSubmitBtn
        title="선택 완료"
        onClick={onClickHandler}
        // disabled={disabled}
      />
    </div>
  );
}

export default ChooseRequest;
