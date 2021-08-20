const dizi =[1,2,3,4]

const kareDizisi=[]

dizi.forEach(sayi =>
{
    kareDizisi.push(sayi*sayi);
})

console.log(kareDizisi)

//map


const mapDizi = dizi.map(sayi=> sayi*3 )
   
console.log(mapDizi);


//filter

const filteriDizi=dizi.filter(sayi=>sayi>2)
console.log(filteriDizi)


//reduce

const toplam = dizi.reduce((acc,sayi)=>{

    return acc+sayi;

})
console.log(toplam)