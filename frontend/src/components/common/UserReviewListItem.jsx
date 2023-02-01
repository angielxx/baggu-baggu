import React from 'react';
//forstaging

function UserReviewListItem({ reviewTitle, reviewNumber }) {
  return (
    <div>
      <span>{reviewTitle}</span>
      <span>{reviewNumber}</span>
    </div>
  );
}

export default UserReviewListItem;
