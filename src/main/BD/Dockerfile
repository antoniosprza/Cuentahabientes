# use of an ubuntu base for simplicity and transparency

FROM ubuntu:18.04
MAINTAINER Antonio Esparza

# getting postgres
RUN apt-get update && apt-get -y install wget gnupg2
RUN wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | apt-key add -

# Add PostgreSQL's repository. It contains the most recent stable release
#     of PostgreSQL, ``12``.
RUN echo "deb http://apt.postgresql.org/pub/repos/apt/ bionic-pgdg main" > /etc/apt/sources.list.d/pgdg.list

# Install software-properties-common and PostgreSQL 12
#  and some other packages for ftp
RUN apt-get update
RUN DEBIAN_FRONTEND=noninteractive apt-get install -y \
  software-properties-common \
  postgresql-12 \
  postgresql-client-12 \
  postgresql-contrib-12 \
  postgresql-12-postgis-3 \
  postgresql-12-postgis-3-scripts \
  aptitude  \
  unzip \
  openssh-client \
  openssh-server \
  sshpass \
  && aptitude update \
  && aptitude install -y nano axel wput screen p7zip-full osmium-tool \
  vnstat gdal-bin


WORKDIR /home/gonet
RUN mkdir /home/gonet/pbf
RUN mkdir /home/gonet/gpkg
COPY ./datos.csv /home/gonet
COPY ./mapping.yml /home/gonet/pbf
RUN ["chown" , "-R", "postgres:postgres", "/home/gonet"]
RUN ["chmod", "+x", "/home/gonet/pbf/import_gonet.sh"]


## download imposm3
RUN wget https://github.com/omniscale/imposm3/releases/download/v0.10.0/imposm-0.10.0-linux-x86-64.tar.gz -P /home/gonet &&\
    tar -xf /home/gonet/imposm-0.10.0-linux-x86-64.tar.gz &&\
    cp -R /home/gonet/imposm-0.10.0-linux-x86-64/* /home/gonet/pbf/ &&\
    rm -R /home/gonet/imposm-0.10.0-linux-x86-64


# switch USER
USER postgres

# Adjust PostgreSQL configuration so that remote connections to the
# database are possible.
RUN echo "host all  all    0.0.0.0/0  md5" >> /etc/postgresql/12/main/pg_hba.conf

# And add ``listen_addresses`` to ``/etc/postgresql/12/main/postgresql.conf``
RUN echo "listen_addresses='*'" >> /etc/postgresql/12/main/postgresql.conf

# Expose the PostgreSQL port
EXPOSE 5433

# Create a PostgreSQL role named ``data`` with ``datapasswd`` as the password and
# then create a database `gonet` owned by the ``data`` role and add
# the postgis extension

RUN    /etc/init.d/postgresql start &&\
    psql --command "CREATE USER data WITH SUPERUSER PASSWORD 'datapasswd';" &&\
    createdb -O data gonet &&\
    psql -d gonet --command "CREATE TABLE cuentahabientes (id integer NOT NULL,cliente character varying(150),numero_cuenta character varying(16),usuario character varying(16),clave character varying(64),ultimo_acceso time with time zone,mensaje_encriptado character varying(255),CONSTRAINT accounts_pkey PRIMARY KEY (id));" &&\
    psql -d gonet --command "COPY cuentahabientes FROM '/home/gonet/datos.csv' DELIMITER ',' CSV HEADER;"


# Add VOLUMEs to allow backup of config, logs and databases
VOLUME  ["/etc/postgresql", "/var/log/postgresql", "/var/lib/postgresql", "/home/gonet/gpkg"]

# Set the default command to run when starting the container
CMD ["/usr/lib/postgresql/12/bin/postgres", "-D", "/var/lib/postgresql/12/main", "-c", "config_file=/etc/postgresql/12/main/postgresql.conf"]
