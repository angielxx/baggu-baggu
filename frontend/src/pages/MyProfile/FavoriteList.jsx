import React from 'react';
import FavoriteListItem from './FavoriteListItem';
//forstaging
function FavoriteList({ items }) {
  return (
    <div>
      {items.map(item => (
        <div key={item.id}>
          <FavoriteListItem item={item} />
        </div>
      ))}
    </div>
  );
}

export default FavoriteList;
