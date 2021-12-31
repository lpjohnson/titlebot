import './App.css'
import axios from 'axios'
import Button from '@mui/material/Button'
import TextField from '@mui/material/TextField'
import styled from "styled-components/macro"
import List from '@mui/material/List'
import ListItem from '@mui/material/ListItem'
import React, { useEffect, useState } from "react"
import FontStyles from "./fontStyles"

export const StyledApp = styled.div`
  background-color: #282c34;
  height: 100vh;
  font-family: 'Roboto', sans-serif;
  color: white;
  a {
    color: white;
    text-decoration: none;
  }
  input {
    color: white;
  }
`
export const StyledRow = styled.div`
  display: flex;
  flex-direction: row;
  gap: 10px;
  align-items: center;
`

export const StyledForm = styled.form`
  display: flex;
  flex-direction: row;
  gap: 32px;
  justify-content: center;
`

export const StyledTitle = styled.div`
  font-size: 22px;
  padding: 10px 0 10px;
`

export const StyledError = styled.div`
  color: red;
  padding: 10px 0 10px;
`

function App() {

  const [siteList, setSiteList] = useState([])
  const [error, setError] = useState(null)

  useEffect(() => {
    axios.get(`http://localhost:9000/get-sites`)
    .then(response => {
      setSiteList(response.data)
    })
  }, [])

  const addUrl = (event) => {
    setError(null)
    event.preventDefault()
    const enteredUrl = event.target[0].value
    axios.post(`http://localhost:9000/add`, { url: enteredUrl })
      .then(response => {
        setSiteList([...siteList, response.data])
      })
      .catch(() => {
        setError("Please enter a valid URL!")
      })
  }

  return (
    <React.StrictMode>
      <FontStyles />
      <StyledApp className="App">
        <StyledTitle>
          Behold, a list of site titles with favicons!
        </StyledTitle>
        <StyledForm onSubmit={addUrl}>
          <TextField size="small" id="outlined-basic" variant="outlined" name="url" />
          <Button type ={"submit"} variant={"contained"}>Add a URL</Button>
        </StyledForm>
        {!!error && (
          <StyledError>{error}</StyledError>
        )}
        <List>
          {siteList.length && siteList.map((site) =>
            <ListItem button divider>
              <StyledRow>
                {!!site.iconUrl ? <img width={32} height={32} src={site.iconUrl} alt={site.title}/> : null}
                <a href={site.url}>{site.title}</a>
              </StyledRow>
            </ListItem>
          )}
        </List>
      </StyledApp>
    </React.StrictMode>
  )
}

export default App
