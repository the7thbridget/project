FROM node:alpine

WORKDIR /d/OMSCS/6310/submit

COPY package.json .
COPY package-lock.json .

RUN npm install

COPY . .

EXPOSE 4200

CMD /d/OMSCS/6310/submit/node_modules/.bin/ng serve --host 0.0.0.0 --disable-host-check