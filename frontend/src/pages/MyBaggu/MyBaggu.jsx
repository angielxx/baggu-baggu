import React from 'react';
import ProductList from 'components/common/ProductList';
import { useEffect, useState } from 'react';

function MyBaggu() {
  const [movies, setMovies] = useState([]);
  const getMovies = async () => {
    const json = await (
      await fetch(
        `https://yts.mx/api/v2/list_movies.json?minimum_rating=8.8&sort_by=year`
      )
    ).json();
    setMovies(json.data.movies);
  };
  useEffect(() => {
    getMovies();
  }, []);
  return <ProductList movies={movies} />;
}

export default MyBaggu;
