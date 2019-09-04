**Steps for running the project**


1. Just run this command `docker-compose up --build `

     `[This build the image of project, deploy it in container and run the container]`
     
     
**Operations**

To Check Rest Api End, Use Postmen Client and hit the end point.

 1. To Search Movie
 
   just hit  => localhost:8888/api/search?keyword=avengers 
   sample json:
   
      `{
       "movieDetailsList": [
           {
               "Title": "The Avengers",
               "Year": "2012",
               "imdbID": "tt0848228",
               "Type": "movie",
               "Poster": "https://m.media-amazon.com/images/M/MV5BNDYxNjQyMjAtNTdiOS00NGYwLWFmNTAtNThmYjU5ZGI2YTI1XkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_SX300.jpg"
           }
            ],
              "totalResults": 1
      }
     `