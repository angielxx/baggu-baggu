import React from 'react';
import axios from 'axios';
import { useQuery } from 'react-query';
import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import TopBar2 from '../../components/common/TopBar2';
import UserInfo from 'components/common/UserInfo';
import BagguOfferList from './BagguOfferList';
//상태관리 사용해서 구현
function Item() {
  const { id } = useParams();
  // const [movie, setMovie] = useState([]);
  const { data: movie, status } = useQuery('getMovie', async () => {
    const response = await axios.get(
      `https://yts.mx/api/v2/movie_details.json?movie_id=${id}`
    );
    return response.data.data.movie;
  });
  // async function getMovie() {
  //   try {
  //     //응답 성공
  //     const json = await axios.get(
  //       `https://yts.mx/api/v2/movie_details.json?movie_id=${id}`
  //     );
  //     console.log(json);
  //     setMovie(json.data.data.movie);
  //   } catch (error) {
  //     //응답 실패
  //     console.error(error);
  //   }
  // }
  // useEffect(() => {
  //   getMovie();
  // }, []);

  return (
    <div>
      <TopBar2 />
      <UserInfo user="user" />
      <div>
        {status === 'loading' && <div>Loading...</div>}{' '}
        {status === 'success' && <img src={movie.medium_cover_image} alt="" />}
        {/* {status === 'success' && <BagguOfferList offers={movie} 66/>}} */}
      </div>
      {/* <img src={movie.medium_cover_image} alt="" />
      <BagguOfferList offers={movie} /> */}
    </div>
  );
}

export default Item;
