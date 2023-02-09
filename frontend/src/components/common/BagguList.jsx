import React from 'react';
import BagguListItem from './BagguListItem';
import tw, { styled, css } from 'twin.macro';
import { useNavigate, useLocation } from 'react-router-dom';

const ListWrapper = styled.div`
  ${tw`border-t-4 overflow-scroll`}
  ${css`
    height: calc(100vh - 218px);
  `}
`;
function BagguList({ baggus }) {
  const location = useLocation().pathname;
  return (
    <ListWrapper
      id="list-wrapper"
      className={`${
        location.startsWith('/myprofile') ? 'mt-[0px]' : 'mt-[60px]'
      }`}
    >
      {baggus
        ? baggus.map(baggu => (
            <div key={baggu.itemIdx}>
              <BagguListItem baggu={baggu} />
            </div>
          ))
        : ''}
      <hr />
    </ListWrapper>
  );
}

export default BagguList;
