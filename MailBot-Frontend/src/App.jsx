import { useState } from 'react'
import axios from 'axios';  // Importing Axios
import './App.css'
import { Container , Typography, Box, TextField, FormControl, InputLabel, Select, MenuItem, Button, CircularProgress} from '@mui/material';

function App() {

  const [emailContent, setemailContent] = useState("");
  const [tone, settone] = useState("");
  const [generatedReply, setgeneratedReply] = useState("");
  const[loading, setLoading] = useState(false); 
  const [error, seterror] = useState("");


  const submitHandler = async ()=>{
        setLoading(true); 
        seterror('');

        try {
          
          const response = await axios.post('https://mailbot-backend.onrender.com', {
            emailContent: emailContent,
            tone: tone
          });
            
          setgeneratedReply(typeof response.data === 'string' ? response.data : JSON.stringify(response.data));

        } catch (error) {
          seterror(`Failed to generate email reply. Please try again. Error: ${error}`)
        } finally{
          setLoading(false);
        }

  }

  return (
    <>
      <Container maxWidth='md' sx={{py:4}}>
            <Typography variant='h4' gutterBottom component={"h1"} sx={{textAlign:'center'}}>
                  MailBot - AI Backed Email Reply Generator
            </Typography>

            <Box>
              <TextField fullWidth multiline rows={6} variant='outlined' label={"Original Email Content"} onChange={(e)=>{
                setemailContent(e.target.value)
              }} sx={{mb:2}} />
            </Box>

            <FormControl fullWidth sx={{mb:2}}>
              <InputLabel>Tone (Optional)</InputLabel>
              <Select 
              value={tone || ''}
              label={"Tone (Optional"}
              onChange={(e)=>{settone(e.target.value)}}
              >
                <MenuItem value={""}>None</MenuItem>
            <MenuItem value={"Professional"}>Professional</MenuItem>
            <MenuItem value={"Friendly"}>Friendly</MenuItem>
            <MenuItem value={"Casual"}>Casual</MenuItem>
            <MenuItem value={"Diplomatic"}>Diplomatic</MenuItem>

              </Select>
            </FormControl>


              <Button
              fullWidth
              variant='contained'
              onClick={submitHandler}
              disabled = {!emailContent}
              >
                {loading? <CircularProgress size={24} /> : "Generate Reply"}

              </Button>

              {
                error && (
                  <Typography color='error' align='center' sx={{mt:4, fontSize:15 , borderRadius:50}}>
                    {error}
                  </Typography>
                )
              }

              {
                generatedReply && (
                  <Box sx={{mt:3}}>
                    <Typography variant='h6' gutterBottom>
                        Generated Reply :
                    </Typography>

                    <TextField
                    fullWidth multiline rows={6}
                    variant='outlined' 
                    value={generatedReply || ''}
                    inputProps={{readOnly: true}} />

                    <Button variant='outlined' sx={{mt:2}} onClick={()=>{
                      navigator.clipboard.writeText(generatedReply)
                    }} >Copy to Clipboard</Button>
                  </Box>
                )
              }

      </Container>

    </>
  )
}

export default App
