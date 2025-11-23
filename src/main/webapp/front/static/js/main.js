

console.log("SwiftCart frontend loaded.");

async function checkBackend(){
    try{
        let res = await fetch("/SwiftCart/");
        console.log("Backend status:", res.status);
    }catch(e){
        console.log("Backend unreachable:", e);
    }
}

checkBackend();
