# RiseUp Summit 2017 - Functional Programming Deep Dive

Functional Programming Deep Dive @ https://riseupsummit.com/

## Abstract

Functional Programming is almost main stream. With the wide adoption of libraries and frameworks like Spark, Kafka and Flink to name a few, it is imperative for IT professionals and technical architects to get familiar with the concept and why it matters.
In this Deep Dive, we will introduce some of the main concepts in functional programming and how it is being used at elmenus.com, we will then dive into details like building a fully functional stack for typical web applications and services.

Here are some of the concepts we will discuss:

- What is Functional Programming and why it matters
- Small introduction to Scala programming language
- Building services that blends Functional and Object-Oriented code
- Domain modeling in functional programming
- Functional database access
- Implementing secure business services using functions
- Streaming and processing data in real-time using functions 

By the end of this session, you will learn how to implement an event streaming processor from your favorite data store to WebSocket clients.

## Quick Environment Setup

- Install [Vagrant](https://www.vagrantup.com) for your platform
- Install [Ansible](https://www.ansible.com) for your platform
- Inside `deploy` directory run `vagrant up`
- Run `ansible-playbook --inventory-file=local -v --become -e "dropdb=yes" --private-key=.vagrant/machines/default/virtualbox/private_key db.yml`
- If you would like to run the examples discussed in the workshop you need [SBT](http://www.scala-sbt.org) and [Node](https://nodejs.org/en/)
- Scala examples are under `code/riseup-summit-2017`
- A command-line Node based WebSocket client is under `code/client`
- Elm based web application with a WebSocket client is under `code/web`
- Instructions to run the examples are under their respective directories

## Slides

Slides are in `slides` directory, inside `slides` directory, run:

```bash
npm i
npm run slides
```

Slides are now served through `http://localhost:8080`

