import React from 'react';
import tw, { styled, css } from 'twin.macro';
//forstaging

const Container = tw.div`w-full h-[60px] p-2 text-h3 border-b fixed bg-white`;

function HeadingBar({ title }) {
  return (
    <Container>
      <h3>{title}</h3>
    </Container>
  );
}

export default HeadingBar;
