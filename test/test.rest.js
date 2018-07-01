"use strict";

const expect = require("chai").expect;

let chai = require('chai');
const chaiHttp = require('chai-http');
chai.use(chaiHttp);
let should = chai.should();

let BASE_URL = process.env.BASE_URL;
let POST_RECIPE_PATH    = "/recipes";
let GET_ALL_RECIPE_PATH = "/recipes";
let GET_ONE_RECIPE_PATH = "/recipes/";
let UPDATE_RECIPE_PATH  = "/recipes/";
let DELETE_RECIPE_PATH  = "/recipes/";


let recipeIdList = [];

describe('/GET recipes', () => {
  it('should GET all the recipes', (done) => {
    chai.request(BASE_URL)
    .get(GET_ALL_RECIPE_PATH)
    .end((err, res) => {
      expect(res).to.have.status(200);
      expect(res).to.be.a('object');
      for (let i=0; i < res.body.recipes.length; i++) {
        recipeIdList.push(res.body.recipes[i].id);
      }
      done();
    });
  });
});

describe('/POST recipes', () => {
  it('should not POST recipe when title, making_time, serves, ingredients, cost params are missing', (done) => {
    let recipe = {
    }
    chai.request(BASE_URL)
    .post(POST_RECIPE_PATH)
    .send(recipe)
    .end((err, res) => {
      res.should.have.status(200);
      res.body.should.be.a('object');
      res.body.should.have.property('message').eql('Recipe creation failed!');
      done();
    });
  });
  it('should POST a recipe ', (done) => {
    let recipe = {
      "title": "トマトスープ",
      "making_time": "15分",
      "serves": "5人",
      "ingredients": "玉ねぎ, トマト, スパイス, 水",
      "cost": 450
    }
    chai.request(BASE_URL)
    .post(POST_RECIPE_PATH)
    .send(recipe)
    .end((err, res) => {
      res.should.have.status(200);
      res.body.should.be.a('object');
      res.body.should.have.property('message').eql('Recipe successfully created!');
      done();
    });
  });
});

describe('/GET/{id} recipe', () => {
  it('should GET a recipe by the given id', (done) => {
    chai.request(BASE_URL)
    .get(GET_ONE_RECIPE_PATH + recipeIdList[0])
    .end((err, res) => {
      res.should.have.status(200);
      res.body.should.be.a('object');
      res.body.should.have.property('message').eql('Recipe details by id');
      done();
    });
  });
});

describe('/PATCH/{id} recipe', () => {
  it('should UPDATE a recipe ', (done) => {
    let recipe = {
      "title": "オムライス",
      "making_time": "20分",
      "serves": "7人",
      "ingredients": "玉ねぎ,卵,スパイス,醤油",
      "cost": 400
    }
    chai.request(BASE_URL)
    .patch(UPDATE_RECIPE_PATH + recipeIdList[0])
    .send(recipe)
    .end((err, res) => {
      res.should.have.status(200);
      res.body.should.be.a('object');
      res.body.should.have.property('message').eql('Recipe successfully updated!');
      done();
    });
  });
});

describe('/DELETE/{id} recipe', () => {
  it('should DELETE a recipe ', (done) => {
    chai.request(BASE_URL)
    .delete(DELETE_RECIPE_PATH + recipeIdList[0])
    .end((err, res) => {
      res.should.have.status(200);
      res.body.should.be.a('object');
      res.body.should.have.property('message').eql('Recipe successfully removed!');
      done();
    });
  });
});
