import { createGlobalStyle } from "styled-components"
import RobotoWoff from "./fonts/roboto-v29-latin-regular.woff"
import RobotoWoff2 from "./fonts/roboto-v29-latin-regular.woff2"

const FontStyles = createGlobalStyle`

@font-face {
  font-family: 'Roboto Condensed';
  src: url(${RobotoWoff2}) format('woff2'),
       url(${RobotoWoff}) format('woff');
}
`

export default FontStyles
