FROM nginx:1.27-alpine

RUN rm /etc/nginx/conf.d/default.conf

COPY nginx.conf /etc/nginx/nginx.conf

RUN mkdir -p /etc/nginx/certs

EXPOSE 80 443

CMD ["nginx", "-g", "daemon off;"]
