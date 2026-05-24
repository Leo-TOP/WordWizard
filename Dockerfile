FROM ubuntu:latest
LABEL authors="levga"

ENTRYPOINT ["top", "-b"]