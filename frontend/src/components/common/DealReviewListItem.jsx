import React from 'react';
//forstaging

function DealReviewListItem({ review }) {
  return (
    <div>
      <img src={review.medium_cover_image} alt="user1_profile_image" />
      <span>{review.summary}</span>
    </div>
  );
}

export default DealReviewListItem;
