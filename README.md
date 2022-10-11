"# GDrive_Downloader" 


 sudo docker run --name downloader -v /srv/dev-disk-by-uuid-928A067C8A065CDB/Downloads:/Downloads -v /srv/dev-disk-by-uuid-928A067C8A065CDB/Config/Downloader:/data -e GOOGLE_CLIENT_ID='{}' -e GOOGLE_CLIENT_SECRET='{}' -p 9090:8080 -m 250m talasila/downloader
