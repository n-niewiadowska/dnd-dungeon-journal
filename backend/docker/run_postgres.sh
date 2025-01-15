#!/bin/bash

docker run --name postgres -e POSTGRES_PASSWORD=pass -d -p 5432:5432 postgres