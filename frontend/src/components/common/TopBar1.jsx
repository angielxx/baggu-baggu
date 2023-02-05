import React from 'react';
import { Link, useLocation } from 'react-router-dom';

// icon
import search from '../../assets/icons/search.svg';
import notification from '../../assets/icons/notification.svg';
import locationIcon from '../../assets/icons/location.svg';
import tw, { styled } from 'twin.macro';

const Container = tw.div`flex fixed justify-between p-2 border-b bg-white border-grey1 h-[60px] w-full`;

function TopBar1() {
  const town = '역삼동';

  // 온보딩 페이지에서 상단바 숨기기
  const location = useLocation().pathname;
  if (
    location.startsWith('/start') ||
    location.startsWith('/chat') ||
    location.startsWith('/bagguReview') ||
    location.startsWith('/makeRequest') ||
    location.startsWith('/makeRequest') ||
    location.startsWith('/myprofile') ||
    location.startsWith('/userReview')
  ) {
    return null;
  }
  if (location.startsWith('/user')) {
    return null;
  }
  return (
    <Container
      id="top-bar-1"
      className="flex flex-wrap justify-between p-2 border-b border-grey1 h-[60px]"
    >
      <div>
        {/* 동네설정 url은 임시 */}
        <Link to="/town" className="flex items-center">
          <img
            src={locationIcon}
            alt="icon-search"
            className="w-[40px] h-[40px]"
          />
          <span className="font-pretendard text-h3 text-primary">{town}</span>
        </Link>
      </div>
      <div className="flex">
        <Link to="/search">
          <img src={search} alt="icon-search" className="w-[40px] h-[40px]" />
        </Link>
        <Link to="/notification" className="relative w-[40px] h-[40px]">
          <img
            src={notification}
            alt="icon-notification"
            className="absolute w-[40px] h-[40px]"
          />
          <div className="absolute right-0 rounded-full bg-secondary w-[12px] h-[12px]"></div>
        </Link>
      </div>
    </Container>
  );
}

export default TopBar1;
