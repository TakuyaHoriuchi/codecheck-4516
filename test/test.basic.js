"use strict";

const expect = require("chai").expect;
const codecheck = require("codecheck");
const validUrl = require("valid-url");

let chai = require('chai');
const chaiHttp = require('chai-http');
chai.use(chaiHttp);

let BASE_URL = process.env.BASE_URL;


describe("codecheck.yml BASE_URL", () => {
  it("contains a valid URL", () => {
    // "記述されたURLが無効な形式です。"
    expect(validUrl.isUri(BASE_URL)).to.be.ok;
  });
});

describe("API server", () => {
  it(`returns status code 200 when accessing ${BASE_URL}/`, (done) => {
    chai.request(BASE_URL)
    .get("/")
    .end((err, res) => {
      expect(err).to.be.null;
      expect(res).to.have.status(200);
      done();
    });
  });
});
