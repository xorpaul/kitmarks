mysql -urecommender -pCR4TB3 -h squall -e "SELECT linkClientID, linkUrlID from chipmark.link" > $1webapps/root/src/bookmarks/recommender/exportRecommendations.out
