<template >
  <div class="calculator">
    <div class="result-container">
      {{num1}} {{op}} {{num2}}
    </div>
    <div class="row">
      <button @click="doSpecialOp('percent')" class="op-button">%</button>     
      <button @click ="clear()" class="op-button">CE</button> 
      <button @click ="clear()" class="op-button">C</button> 
      <button @click ="remove()" class="op-button">DEL</button>
    </div>

    <div class="row">
      <button @click="doSpecialOp('reciprocal')" class="op-button">1/x</button>
      <button @click="doSpecialOp('square')" class="op-button">x<sup>2</sup></button>
      <button @click="doSpecialOp('root')" class="op-button">&#8730;<span style="text-decoration:overline;">x </span></button>
      <button @click ="addToExpression('/')" class="op-button"> &#247;</button>
    </div>

    <div class="row">
      <button @click ="addToExpression('7')" class="normal-button">7</button>
      <button @click ="addToExpression('8')" class="normal-button">8</button>
      <button @click ="addToExpression('9')" class="normal-button">9</button>
      <button @click ="addToExpression('*')" class="op-button">x</button>
    </div>

    <div class="row">
      <button @click ="addToExpression('4')" class="normal-button">4</button>
      <button @click ="addToExpression('5')" class="normal-button">5</button>
      <button @click ="addToExpression('6')" class="normal-button">6</button>
      <button @click ="addToExpression('-')" class="op-button">-</button>
    </div>

    <div class="row">
      <button @click ="addToExpression('1')" class="normal-button">1</button>
      <button @click ="addToExpression('2')" class="normal-button">2</button>
      <button @click ="addToExpression('3')" class="normal-button">3</button>
      <button @click ="addToExpression('+')" class="op-button">+</button>
    </div>

    <div class="row">
      <button @click="changeSign()" class="normal-button">&#xb1;</button>
      <button @click ="addToExpression('0')" class="normal-button">0</button>
      <button @click="handleDecimalPoint()" class="normal-button">.</button>
      <button @click ="evaluate()" class="equal-button">=</button>
    </div>    

  </div>
</template>

<script >
  export default{
    data(){
      return{
        num1: '',
        op: '',
        num2 : '',
        isResult1: false,
        isResult2: false
      }
    },
    methods:{

      async addToExpression(input){
        //handle error
        if(this.num1 === 'Error')
          this.clear()

        //handle operations directly without operands
        if(this.handleNoOperands(input) || this.handleOnlyOneDot(input))
          return 

        //after a result
        if(this.isResult1 && this.op === ''){    
          if(this.checkOp(input))
            this.op = input
          else {   //new operands
            this.clear()
            this.num1 = '' + input
          }
            this.num2 = ''
        }

        else if(this.isResult2 && this.op !== '' && !this.checkOp(input)){
          this.clear()
          this.num1 += input          
        }

        //multiple operations
        else if( this.checkOp(input) && this.num2 !=='' ){
          await this.evaluate()   
          this.op = input
        }

        else if(this.isResult1 && this.isResult2){
          return 
        }

        //normal opeartion
        else{
          if(this.checkOp(input))
            this.op = input
          else if(this.op !== '')
            this.num2 += input
          else
            this.num1 += input
        }
      },

      checkOp(input){
        return input === '*' || input === '+' || input === '-' || input === '/'
      },
      checkSpecialOp(input){
        return input === 'square' || input === 'root' || input === 'percent' || input === 'reciprocal' 
      },

      async evaluate(){
        if(this.num1 === 'Error')
          this.clear()

        if(this.num2 === ''){
          this.op = ''  
          return
        }

        let exp = [this.num1, this.op, this.num2]

        await fetch("http://localhost:8081/normalOp",{
          method: "POST",
          headers: {
            'content-type': 'application/json',            
          },
          body: JSON.stringify(exp)
        }).then(res =>{
          return res.json()
        }).then(data =>{
          if(this.handleError(data))
            return

          this.num1 = '' + data
          this.isResult1 = true
          this.isResult2 = false
          this.num2 = ''
          this.op = ''
        }).catch(err =>{
          console.log(err)
        })
      },

      remove(){
        if(this.num1 === 'Error'){
          this.clear()
          return
        }

        if(this.num2 !== '' && !this.isResult2){
          this.num2 = this.num2.substring(0, this.num2.length-1)
          if(this.num2 === '-')
            this.num2 = ''
        }
        else if(this.op !== '' && this.num2 === '')
          this.op = ''
        else if(this.num1 !== '' && !this.isResult1){
          this.num1 = this.num1.substring(0, this.num1.length-1)
          if(this.num1 === '-')
            this.num1 = ''
        }
      },

      doSpecialOp(specialOp){
        if(this.num1 === 'Error'){
          this.clear()
          return
        }
        if(this.handleNoOperands(specialOp) || this.handleOnlyOneDot(specialOp))
          return 

        let operand = 0
        if(this.num2 !== '')
          operand = this.num2
        else {
          operand = this.num1
          this.op = ''
        }

        let exp = [operand, specialOp]

        fetch("http://localhost:8081/specialOp",{
          method: "POST",
          headers: {
            'content-type': 'application/json',            
          },
          body: JSON.stringify(exp)
        }).then(res =>{
          return res.json()
        }).then(data =>{
            if(this.handleError(data))
              return
            
            if(operand === this.num2){
              this.num2 = ''+ data
              this.isResult2 = true
            }  
            else{
              this.num1 = '' + data
              this.isResult1 = true
            }
        }).catch(err =>{
          console.log(err)
        })

      },
      clear(){
        this.num1 = ''
        this.num2 = ''
        this.op = ''
        this.isResult1 = false
        this.isResult2 = false
      },
      handleError(test){
        if(test === 'Infinity' || test === 'NaN'){
          this.clear()
          this.num1 = 'Error'
          return true  
        }
        return false  
      },

      handleDecimalPoint(){
        //handle error
        if(this.num1 === 'Error')
          this.clear()

        if(this.op !== '' && !this.num2.includes('.') && !this.isResult2)
          this.num2 += '.'
        else if(this.op === '' && !this.num1.includes('.') && !this.isResult1)
          this.num1 += '.'
      },
      handleNoOperands(input){
        if(this.num1 === '' && (this.checkOp(input) || this.checkSpecialOp(input))){
          return true
        }
        return false
      },
      handleOnlyOneDot(input){
        //handle error
        if(this.num1 === 'Error'){
          this.clear()
          return
        }
        if(this.num1 === '.' && (this.checkOp(input) || this.checkSpecialOp(input))){
          return true
        }
        return false
      },
      changeSign(){
        //handle error
        if(this.num1 === 'Error'){
          this.clear()
          return
        }
        if(this.op !== '' && this.num2 !== '')
          this.num2 = '' + this.num2 * -1
        else if(this.op === '' && this.num1 !== '')
          this.num1 = '' + this.num1 * -1
          
      },
    }
  }
</script>


<style>
  @import url('https://fonts.googleapis.com/css2?family=Croissant+One&family=Roboto:wght@400;500;700&display=swap');

  sup {font-size:xx-small; vertical-align:top;}
  body{
    font-family: Roboto, Arial;
  }
  .result-container{
    margin: 10px;
    height: 100px;
    color: white;
    display: flex;
    align-items: center;
    padding-right: 10px;
    font-size: 23px;
    font-weight: bold; 

  }
  .calculator{
    border: 1px solid rgb(110, 109, 109);
    border-radius: 6px;
    display: flex;
    flex-direction: column;
    margin: 5% auto;
    height: 435px;
    width: 400px;
    
    background-color: rgb(36, 36, 36);
  }
  .row{
    display: flex;
    flex-direction: row;
    margin: 2px 2px 0 2px;
    justify-content: space-between;
  }
  button{
    border: none;
    border-radius: 7px;
    font-size: 18px;
    margin-right: 2px;
    flex: 1;
    align-items:center; 
    justify-content: center;
    height: 50px;
    color: white;
  }

  .normal-button{
    background-color: rgb(70, 70, 70);
  }
  .normal-button:hover{
    background-color: rgb(56, 56, 56);
  }

  .op-button{
    background-color: rgb(56, 56, 56);
  }

  .op-button:hover{
    background-color: rgb(70, 70, 70);
  }

  .equal-button{
    background-color: rgb(77, 202, 224);
    color: black;
  }

  .normal-button:active, .op-button:active{
    background-color: rgb(46, 46, 46);
  }

  .equal-button:hover{
    background-color: rgb(63, 167, 185)
  }

  .equal-button:active{
    background-color: rgb(55, 148, 165)
  }

</style>